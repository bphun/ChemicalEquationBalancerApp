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
        logger.error("error parsing json")
        result = DockerhubWebhookCallback("error", "unable to parse request json", "docker container auto reload service", "n/a")

    return result

def reloadImage(requestBody):
    imageName = "{0}:{1}".format(requestBody.repository.repo_name, requestBody.push_data.tag)

    logger.info("image named {0} pushed to repo by {1} at {2}".format(imageName, requestBody.push_data.pusher, toTimeStr(requestBody.push_data.pushed_at)))

    client = docker.from_env()

    containerPorts = None

    for container in client.containers.list():
        if container.image.tags[0] == imageName:
            try:
                containerPorts = container.ports
                container.kill()
                logger.debug("killed container with image {}".format(imageName))
                break
            except Exception as e:
                logger.error("error killing {}".format(imageName))
                return DockerhubWebhookCallback("error", "unable to kill {}. error: {}".format(imageName, e), "docker container auto reload service", "n/a")

    portMappings = {}
    if containerPorts:
        for containerport, hostport in containerPorts.items():
            portMappings[containerport] = hostport
    else:
        logger.error("Image with name {} not found".format(imageName))
        return DockerhubWebhookCallback("error", "Image with name {} not found".format(imageName), "docker container auto reload service", "n/a")

    logger.info("pulling latest version of {}".format(imageName))
    client.images.pull(imageName)
    try:
        container = client.containers.run(imageName, detach=True, ports=portMappings, restart_policy={"name": "always"}, )
        logger.info("restarted {} with latest revision of image".format(imageName))
    except Exception as e:
        logger.info("failed to start container with image {}. error: {}".format(imageName, e))
        return DockerhubWebhookCallback("error", "unable to start container with image {}. error: {}".format(imageName, e), "docker container auto reload service", "n/a")

    return DockerhubWebhookCallback("success", "reloaded {} successfully".format(imageName), "docker container auto reload service", "n/a")

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
