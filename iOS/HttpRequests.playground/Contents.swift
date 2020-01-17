let userRequest = ApiRequest(resource: UserResource())

userRequest.get { (users: [User]?) in
    if let users = users {
        print(users.map({ "\($0)" }).joined(separator: "\n"))
    }
}

//import Foundation
//
//class HttpSession {
//
//    var hostUrl: String
//    var urlSession: URLSession
//
//    init(hostUrl: String) {
//        self.hostUrl = hostUrl
//        self.urlSession = URLSession(configuration: .default)
//    }
//
//    func get(path: String? = nil, query: String? = nil, requestCompletionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) {
//        if var urlComponents = URLComponents(string: self.hostUrl) {
//            if let path = path { urlComponents.path = path }
//            if let query = query { urlComponents.query = query }
//
//            guard let url = urlComponents.url else { return }
//
//            let task = urlSession.dataTask(with: url, completionHandler: { (data, response, error) in
//                requestCompletionHandler(data, response, error)
//            })
//            task.resume()
//        }
//    }
//}
//
//HttpSession(hostUrl: "https://learnappmaking.com").get(path: "/ex/users.json") { (data, response, error) in
//    if let error = error {
//        print("error: \(error.localizedDescription)")
//    } else if let data = data, let response = response as? HTTPURLResponse, response.statusCode == 200 {
//        do {
//            let humans = try JSONDecoder().decode([Human].self, from: data)
//            print(humans.map({ "\($0)" }).joined(separator: "\n"))
//        } catch {
//            print("error decoding json \(error.localizedDescription)")
//        }
//    }
//}
