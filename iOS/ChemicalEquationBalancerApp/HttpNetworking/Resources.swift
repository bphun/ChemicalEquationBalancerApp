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

    let hostUrl: String
    let path = "/proxy"
    let method: HTTPMethod = .POST

    var urlRequest: URLRequest

    init(data: Data? = nil, useProdApi: Bool) {
        hostUrl = "http://" + (useProdApi ? Environment.productionApiHostname : Environment.localTestingHostname)
        
        var urlComponents = URLComponents(string: hostUrl)!
        urlComponents.path = path

        urlRequest = URLRequest(url: urlComponents.url!)
        urlRequest.httpMethod = method.rawValue
        urlRequest.httpBody = data
        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
    }
    
    init(data: Data? = nil, shouldUploadImage: Bool, useProdApi: Bool) {
        hostUrl = "http://" + (useProdApi ? Environment.productionApiHostname : Environment.localTestingHostname)
        
        var urlComponents = URLComponents(string: hostUrl)!
        urlComponents.path = path

        let queryItems = [URLQueryItem(name: "upload", value: shouldUploadImage ? "true" : "false")]
        urlComponents.queryItems = queryItems
        
        urlRequest = URLRequest(url: urlComponents.url!)
        urlRequest.httpMethod = method.rawValue
        urlRequest.httpBody = data
        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
    }
    
    init(data: Data? = nil, shouldUploadImage: Bool, equationStr: String?, useProdApi: Bool) {
        hostUrl = "http://" + (useProdApi ? Environment.productionApiHostname : Environment.localTestingHostname)
        
        var urlComponents = URLComponents(string: hostUrl)!
        urlComponents.path = useProdApi ? "/api/v1" + path : path

        var queryItems = [URLQueryItem(name: "upload", value: shouldUploadImage ? "true" : "false")]
        if equationStr != nil {
            queryItems.append(URLQueryItem(name: "eq", value: equationStr!.addingPercentEncoding(withAllowedCharacters: .alphanumerics)))
        }
        print(queryItems)
        urlComponents.queryItems = queryItems
        print(urlComponents.url)
        urlRequest = URLRequest(url: urlComponents.url!)
        urlRequest.httpMethod = method.rawValue
        urlRequest.httpBody = data
        urlRequest.addValue(String(data!.count), forHTTPHeaderField: "Content-Length")
        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
    }
}
