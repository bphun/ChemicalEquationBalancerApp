class DockerhubWebhookCallback(object):
    def __init__(self, state, description, context, target_url):
        self.state = state
        self.description = description
        self.context = context
        self.target_url = target_url

    def serialize(self):
        return {
                "state": self.state,
                "description": self.description,
                "context": self.context,
                "target_url": self.target_url
                }
