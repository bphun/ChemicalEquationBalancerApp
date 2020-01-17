import UIKit

enum FeatureType: String {
    case DocumentTextDetection = "DOCUMENT_TEXT_DETECTION"
}

struct ImageprocessorFeature: Codable {
    var maxResults: Int
    var type: String
    
    enum CodingKeys: String, CodingKey {
        case maxResults = "maxResults"
        case type = "type"
    }
    
    init(maxResults: Int, type: String) {
        self.maxResults = maxResults
        self.type = type
    }
}

struct ImageProcessorImage: Codable {
    var base64EncodedString: String
    
    enum CodingKeys: String, CodingKey {
        case base64EncodedString = "content"
    }
}

struct ImageProcessorRequestBody: Codable {
    var features: [ImageprocessorFeature]
    var image: ImageProcessorImage
    
    enum CodingKeys: String, CodingKey {
        case features = "features"
        case image = "image"
    }
    
    mutating func addFeature(feature: ImageprocessorFeature) {
        features.append(feature)
    }
    
    init(image: ImageProcessorImage) {
        self.features = [ImageprocessorFeature]()
        self.image = image
    }
}

let feature = ImageprocessorFeature(maxResults: 50, type: FeatureType.DocumentTextDetection.rawValue)
let image = ImageProcessorImage(base64EncodedString: "asdfasdfasdf")
var imageProcessorRequestBody = ImageProcessorRequestBody(image: image)

imageProcessorRequestBody.addFeature(feature: feature)

do {
    let jsonData = try! JSONEncoder().encode(imageProcessorRequestBody)
    let json = String(data: jsonData, encoding: .utf8)
    
    print(json)
} catch {
    print(error.localizedDescription)
}
