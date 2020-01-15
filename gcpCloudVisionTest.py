import requests, sys, json, base64

with open("/Users/brandonphan/Desktop/testImg1.png", "rb") as image_file:
    encoded_string = base64.b64encode(image_file.read())

url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCjcvFqGbD6kaQWy9g5kGLxWK0wvhr4l6k"
requestBody = {
  "requests": [
    {
      "features": [
        {
          "maxResults": 50,
          "type": "DOCUMENT_TEXT_DETECTION"
        }
      ],
      "image": {
        "content": encoded_string
      }
    }
  ]
}

requestHeaders = {"Content-Length": str(sys.getsizeof(requestBody)), "Content-Type": "application/json"}
request  = requests.post(url=url, data=json.dumps(requestBody), headers=requestHeaders)
print(json.loads(request.text)["responses"][0]["textAnnotations"][0]["description"])
