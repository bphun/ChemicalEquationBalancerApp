//
//  Resources.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/16/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

import Foundation

enum HTTPMethod: String {
    case GET = "GET"
    case POST = "POST"
    case PUT = "PUT"
    case DELETE = "DELETE"
    case OPTIONS = "OPTIONS"
    case HEAD = "HEAD"
}

protocol ApiResource {
    associatedtype ResponseBodyModelType: Decodable
    
    var hostUrl: String { get }
    var path: String { get }
    var method: HTTPMethod { get }
    var urlRequest: URLRequest { get }
}

struct ImageProcessResource: ApiResource {
    typealias ResponseBodyModelType = ImageProcesserOutput

    let hostUrl = "http://192.168.1.42:8080"
    let path = "/proxy"
    let method: HTTPMethod = .POST

    var urlRequest: URLRequest

    init(data: Data? = nil) {
        var urlComponents = URLComponents(string: hostUrl)!
        urlComponents.path = path
        print(String(data!.count))

        urlRequest = URLRequest(url: urlComponents.url!)
        urlRequest.httpMethod = method.rawValue
        urlRequest.httpBody = data
        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
    }
}

//struct ImageProcessResource: ApiResource {
//    typealias ResponseBodyModelType = ImageProcesserOutput
//
//    let hostUrl = "https://vision.googleapis.com"
//    let path = "/v1/images:annotate"
//    let method: HTTPMethod = .POST
//
//    var urlRequest: URLRequest
//
//    init(data: Data? = nil) {
//        var urlComponents = URLComponents(string: hostUrl)!
//        urlComponents.path = path
//
//        let apiKey = [URLQueryItem(name: "key", value: "AIzaSyCjcvFqGbD6kaQWy9g5kGLxWK0wvhr4l6k")]
//        urlComponents.queryItems = apiKey
//
//        urlRequest = URLRequest(url: urlComponents.url!)
//        urlRequest.httpMethod = method.rawValue
//        urlRequest.httpBody = data
//        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
//        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
//    }
//}

//struct ImageProcessResource: ApiResource {
//    typealias ResponseBodyModelType = ImageProcesserOutput
//
//    let hostUrl = "https://vision.googleapis.com"
//    let path = "/v1/images:annotate"
//    let method: HTTPMethod = .POST
//
//    var urlRequest: URLRequest
//
//    init(data: Data? = nil) {
//        var urlComponents = URLComponents(string: hostUrl)!
//        urlComponents.path = path
//
//        let apiKey = [URLQueryItem(name: "key", value: "AIzaSyCjcvFqGbD6kaQWy9g5kGLxWK0wvhr4l6k")]
//        urlComponents.queryItems = apiKey
//
//        urlRequest = URLRequest(url: urlComponents.url!)
//        urlRequest.httpMethod = method.rawValue
//        urlRequest.httpBody = data
//        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
//        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
//    }
//}
