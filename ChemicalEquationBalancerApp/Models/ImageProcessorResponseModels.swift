//
//  ImageProcessorResponseModels.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 1/9/20.
//  Copyright © 2020 Brandon Phan. All rights reserved.
//

// MARK: - ImageProcesserOutput
struct ImageProcesserOutput: Decodable {
    var responses: [Response]
    
    enum CodingKeys: String, CodingKey {
        case responses = "responses"
    }
}

// MARK: - Response
struct Response: Decodable {
    var textAnnotations: [TextAnnotation]?
    var fullTextAnnotation: FullTextAnnotation?
    var error: ErrorResponse?
    
    enum CodingKeys: String, CodingKey {
        case textAnnotations = "textAnnotations"
        case fullTextAnnotation = "fullTextAnnotation"
        case error = "error"
    }
}

// MARK: - ErrorResponse
struct ErrorResponse: Decodable {
    var message: String?
    var code: Int?
    
    enum CodingKeys: String, CodingKey {
        case message = "message"
        case code = "code"
    }
}

// MARK: - FullTextAnnotation
struct FullTextAnnotation: Decodable {
    var pages: [Page]
    var text: String
    
    enum CodingKeys: String, CodingKey {
        case pages = "pages"
        case text = "text"
    }
}

// MARK: - Page
struct Page: Decodable {
    var width, height: Int
    var blocks: [Block]
    
    enum CodingKeys: String, CodingKey {
        case width = "width"
        case height = "height"
        case blocks = "blocks"
    }
}

// MARK: - Block
struct Block: Decodable {
    var boundingBox: Bounding
    var paragraphs: [Paragraph]
    var blockType: String
    var confidence: Double
    
    enum CodingKeys: String, CodingKey {
        case boundingBox = "boundingBox"
        case paragraphs = "paragraphs"
        case blockType = "blockType"
        case confidence = "confidence"
    }
}

// MARK: - Bounding
struct Bounding: Decodable {
    var vertices: [Vertex]
    
    enum CodingKeys: String, CodingKey {
        case vertices = "vertices"
    }
}

// MARK: - Vertex
struct Vertex: Decodable {
    var x, y: Int?
    
    enum CodingKeys: String, CodingKey {
        case x = "x"
        case y = "y"
    }
}

// MARK: - Paragraph
struct Paragraph: Decodable {
    var boundingBox: Bounding
    var words: [Word]
    var confidence: Double
    
    enum CodingKeys: String, CodingKey {
        case boundingBox = "boundingBox"
        case words = "words"
        case confidence = "confidence"
    }
}

// MARK: - Word
struct Word: Decodable {
    var boundingBox: Bounding
    var symbols: [Symbol]
    var confidence: Double
    
    enum CodingKeys: String, CodingKey {
        case boundingBox = "boundingBox"
        case symbols = "symbols"
        case confidence = "confidence"
    }
}

// MARK: - Symbol
struct Symbol: Decodable {
    var boundingBox: Bounding
    var text: String
    var confidence: Double
    var property: Property?
    
    enum CodingKeys: String, CodingKey {
        case boundingBox = "boundingBox"
        case text = "text"
        case confidence = "confidence"
        case property = "property"
    }
}

// MARK: - Property
struct Property: Decodable {
    var detectedBreak: DetectedBreak?
    
    enum CodingKeys: String, CodingKey {
        case detectedBreak = "detectedBreak"
    }
}

// MARK: - DetectedBreak
struct DetectedBreak: Decodable {
    var type: String
    
    enum CodingKeys: String, CodingKey {
        case type = "type"
    }
}

// MARK: - TextAnnotation
struct TextAnnotation: Decodable {
    var locale: String?
    var textAnnotationDescription: String
    var boundingPoly: Bounding

    enum CodingKeys: String, CodingKey {
        case locale = "locale"
        case textAnnotationDescription = "description"
        case boundingPoly = "boundingPoly"
    }
}
