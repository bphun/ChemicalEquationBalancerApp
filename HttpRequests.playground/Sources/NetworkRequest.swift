import Foundation

protocol NetworkRequest: AnyObject {
    associatedtype ModelType
    
    func decode(_ data: Data) -> ModelType?
    func get(withCompletionHandler completion: @escaping (ModelType?) -> Void)
}

extension NetworkRequest {
    fileprivate func get(_ url: URL, withCompletionHandler completion: @escaping (ModelType?) -> Void) {
        let session = URLSession(configuration: .default, delegate: nil, delegateQueue: .main)
        let task = session.dataTask(with: url, completionHandler: { [weak self] (data: Data?, response: URLResponse?, error: Error?) -> Void in
            guard let data = data else {
                completion(nil)
                return
            }
            completion(self!.decode(data))
        })
        task.resume()
    }
}

public protocol ApiResource {
    associatedtype ModelType: Decodable
    var methodPath: String { get }
    var hostUrl: String { get }
    var queryItems: [URLQueryItem] { get }
}

extension ApiResource {
    var url: URL {
        var components = URLComponents(string: hostUrl)!
        components.path = methodPath
        components.queryItems = queryItems
        return components.url!
    }
}

public class ApiRequest<Resource: ApiResource> {
    let resource: Resource
    
    public init(resource: Resource) {
        self.resource = resource
    }
}

extension ApiRequest: NetworkRequest {
    public func decode(_ data: Data) -> [Resource.ModelType]? {
        do {
            let wrapper = try JSONDecoder().decode([Resource.ModelType].self, from: data)
            return wrapper
        } catch {
            print("\(error.localizedDescription)")
        }
        return nil
    }
    
    public func get(withCompletionHandler completion: @escaping ([Resource.ModelType]?) -> Void) {
        get(resource.url, withCompletionHandler: completion)
    }
}
