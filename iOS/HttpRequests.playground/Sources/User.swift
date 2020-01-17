import Foundation

public class User: Decodable {
    var firstName, lastName: String
    var age: Int
    
    enum CodingKeys: String, CodingKey {
        case firstName = "first_name"
        case lastName = "last_name"
        case age = "age"
    }
    
    init(firstName: String, lastName: String, age: Int) {
        self.firstName = firstName
        self.lastName = lastName
        self.age = age
    }
}

extension User: CustomStringConvertible {
    public var description: String {
        return "\(firstName) \(lastName) \(age)"
    }
}
