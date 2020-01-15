import Foundation

public struct UserResource: ApiResource {
    public typealias ModelType = User
    
    public let methodPath = "/ex/users.json"
    public let hostUrl = "https://learnappmaking.com"
    public var queryItems = [URLQueryItem]()
    
    public init() {}
}

