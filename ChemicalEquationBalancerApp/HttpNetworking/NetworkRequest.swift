//
//  NetworkRequest.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/16/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

import Foundation

protocol NetworkRequest: AnyObject {
    associatedtype ResponseBodyModelType
    
    func decode(_ data: Data) -> ResponseBodyModelType?
    func run(withCompletionHandler completion: @escaping (ResponseBodyModelType?) -> Void)
}

extension NetworkRequest {
    func run(_ urlRequest: URLRequest, session: URLSession, withCompletionHandler completion: @escaping (ResponseBodyModelType?) -> Void) {
        let task = session.dataTask(with: urlRequest) { (data, response, error) in
            guard let data = data else {
                return completion(nil)
            }
            completion(self.decode(data))
        }
        task.resume()
    }
}
