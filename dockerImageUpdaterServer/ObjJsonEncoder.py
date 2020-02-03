from flask.json import JSONEncoder
from DockerhubWebhookCallback import DockerhubWebhookCallback

class ObjJsonEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, DockerhubWebhookCallback):
            return {
                 "state": obj.state,
                 "description": obj.description,
                 "context": obj.context,
                 "target_url": obj.target_url
                 }
        return super(MyJSONEncoder, self).default(obj)
