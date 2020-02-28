from flask import Flask, request
import json, time, docker, sys, logging, requests
from collections import namedtuple
from types import SimpleNamespace as Namespace
from logger import Logger
from DockerhubWebhookCallback import DockerhubWebhookCallback
from ObjJsonEncoder import ObjJsonEncoder

app = Flask(__name__)

logger = Logger(logFilePath="logs/server.log", formatStr="%(asctime)s [$BOLD%(levelname)-14.14s$RESET]  %(message)s")
logging.getLogger('werkzeug').addHandler(logger.getFileHandler())
logging.getLogger('werkzeug').addHandler(logger.getConsoleHandler())

app.json_encoder = ObjJsonEncoder

def toTimeStr(timeMs):
    return time.strftime('%Y-%m-%d %H:%M:%S UTC', time.localtime(timeMs))

def parseJson(data):
    try:
        result = json.loads(data, object_hook=lambda d: namedtuple('x', d.keys())(*d.values()))
    except:
        logger.error("Error parsing json")
        result = DockerhubWebhookCallback("error", "Unable to parse request json", "Docker container auto reload service", "N/A")

    return result

def reloadImage(requestBody):
    imageName = "{0}:{1}".format(requestBody.repository.repo_name, requestBody.push_data.tag)

    logger.info("Image named {0} pushed to repo by {1} at {2}".format(imageName, requestBody.push_data.pusher, toTimeStr(requestBody.push_data.pushed_at)))

    client = docker.from_env()

    containerPorts = None

    for container in client.containers.list():
        if container.image.tags[0] == imageName:
            try:
                containerPorts = container.ports
                container.kill()
                
                logger.debug("Killed container with image {}".format(imageName))
                break
            except Exception as e:
                logger.error("Error killing {}".format(imageName))
                return DockerhubWebhookCallback("error", "Unable to kill {}. error: {}".format(imageName, e), "Docker container auto reload service", "N/A")

    portMappings = {}
    if containerPorts:
        for containerport, hostport in containerPorts.items():
            portMappings[containerport] = hostport
    else:
        logger.error("Image with name {} not found. Starting new container runtime".format(imageName))

    logger.info("Pulling latest version of {}".format(imageName))
    client.images.pull(imageName)
    try:
        container = client.containers.run(imageName, detach=True, network="chemBalancerNet", name=requestBody.push_data.tag, ports=portMappings, restart_policy={"name": "always"}, )
        logger.info("Restarted {} with latest revision of image".format(imageName))
    except Exception as e:
        logger.info("Failed to start container with image {}. error: {}".format(imageName, e))
        return DockerhubWebhookCallback("error", "Unable to start container with image {}. Error: {}".format(imageName, e), "Docker container auto reload service", "N/A")

    return DockerhubWebhookCallback("success", "Reloaded {} successfully".format(imageName), "Docker container auto reload service", "N/A")

@app.route('/hooks', methods=["POST"])
def webhook():
    data = request.data
    parsedJson = parseJson(data)

    if not isinstance(parsedJson, DockerhubWebhookCallback):
        webhookCallbackData = reloadImage(parsedJson)
        #callbackResponse = requests.post(parsedJson.callback_url, json=webhookCallbackData.serialize())

    return "hi"


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8081)
