//
//  ImageProcessorRequestModels.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/16/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

import Foundation

enum FeatureType: String {
    case DocumentTextDetection = "DOCUMENT_TEXT_DETECTION"
}

struct ImageprocessorFeature: Codable {
    let maxResults: Int
    let type: String
    
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
    let base64EncodedString: String
    
    enum CodingKeys: String, CodingKey {
        case base64EncodedString = "content"
    }
}

struct ImageProcessorRequest: Codable {
    let image: ImageProcessorImage
    var features: [ImageprocessorFeature]

    enum CodingKeys: String, CodingKey {
        case features = "features"
        case image = "image"
    }
    
    mutating func addFeature(feature: ImageprocessorFeature) {
        features.append(feature)
    }
    
    init(image: ImageProcessorImage, feature: ImageprocessorFeature) {
        self.features = [feature]
        self.image = image
    }
    
    init(image: ImageProcessorImage, features: [ImageprocessorFeature]) {
        self.features = features
        self.image = image
    }
}

struct ImageProcessorRequestBody: Codable {
    var requests: [ImageProcessorRequest]
    
    mutating func addRequest(request: ImageProcessorRequest) {
        requests.append(request)
    }
    
    init(request: ImageProcessorRequest) {
        requests = [request]
    }
}

