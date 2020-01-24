//import MachO
//
//var startTime: UInt64 = 0
//var endTime: UInt64 = 0
//var vdspSumTime: UInt64
//var arrReduceTime: UInt64
//
//let a: Vector<Float> = [10, 2, 5]
//let b: Vector<Float> = [-1, 3, 4]
//
//startTime = mach_absolute_time()
//sum(a)
//vdspSumTime = mach_absolute_time() - startTime

//let arrA = [10, 2, 5]
//let arrB = [-1, 3, 4]
//
//startTime = 0
//
//startTime = mach_absolute_time()
//arrA.reduce(0, +)
//arrReduceTime =  mach_absolute_time() - startTime
//
//print("Delta time: \(arrReduceTime - vdspSumTime)ns")

//let system: [Double] = [1, 1, -3, 1]
//let systemMatrix = Matrix<Double>(system, rows: 2, columns: 2)

// X3 is free
//let system: [[Double]] = [
//    [1, 0, -5],
//    [0, 1, 1],
//    [0, 0, 0]
//]
//let augmentVector: Vector<Double> = [1, 4, 0]

import Foundation

func isValidChemicalEquation(_ input: String) -> Bool {
    return true
}

func zeroVectorWithSize(_ n: Int) -> Vector<Double> {
    return Vector<Double>.init(repeating: 0.0, count: n)
}

let system: [[Double]] = [
    [1, 0, 1, 4],
    [0, 0, 1, 2]
]
let augmentVector: Vector<Double> = [1, 0]

let systemMatrix = Matrix<Double>(twoDimArr: system)

//print(invert(systemMatrix))
//print("Matrix:\n\(systemMatrix)")
//print("Augmented column: \(augmentVector)")
//
//print("Solution: \(solve(matrix: systemMatrix, for: augmentVector))")

extension StringProtocol {
    public func ranges(of string: Self, options: String.CompareOptions = []) -> [Range<Index>] {
        var result: [Range<Index>] = []
        var startIndex = self.startIndex
        while startIndex < endIndex,
            let range = self[startIndex...].range(of: string, options: options) {
                result.append(range)
                startIndex = range.lowerBound < range.upperBound ? range.upperBound :
                    index(range.lowerBound, offsetBy: 1, limitedBy: endIndex) ?? endIndex
        }
        return result
    }
}

extension Collection where Element: Equatable {
    func indexOf(of element: Element) -> Int? {
        guard let index = firstIndex(of: element) else { return nil }
        return distance(from: startIndex, to: index)
    }
}

func parseEquationSide(equationSide: String) -> [[String : Double]] {
    var output = [[String : Double]]()
    for molecule in equationSide.components(separatedBy: "+") {
        var components = [String : Double]()
        molecule.ranges(of: "([A-Z][a-z]?)\\s*([0-9]*)", options: .regularExpression).map {
            let element = molecule[$0]
            let elementComponents = element.ranges(of: "\\d+|\\D+", options: .regularExpression).map { element[$0] }
            components[String(elementComponents[0])] = elementComponents.count > 1 ? Double(elementComponents[1]) : 1
        }
        output.append(components)
    }
    return output
}

func getUniqueElements(_ compounds: [[String : Double]]) -> Set<String> {
    var elements = Set<String>()
    for compound in compounds {
        compound.keys.forEach {elements.insert($0) }
    }
    return elements
}

func parseChemicalEquation(_ input: String?) -> Matrix<Double>? {
    guard let input = input else { return nil }
    guard isValidChemicalEquation(input) else { return nil }
    
    let equationSides = input.components(separatedBy: "->")
    
    let lhs = parseEquationSide(equationSide: equationSides[0])
    let rhs = parseEquationSide(equationSide: equationSides[1])
    
    print(lhs)
    print(rhs)
    let elements = getUniqueElements(lhs).union(getUniqueElements(rhs))
    print(elements)
    
    let rows = elements.count
    
    var matrixData = [[Double]]()
    for compound in lhs {
        var column = [Double](repeating: 0, count: rows)
        for (key, value) in compound {
            column[elements.indexOf(of: key)!] = value
        }
        matrixData.append(column)
    }
        
    for compound in rhs {
        var column = [Double](repeating: 0, count: rows)
        for (key, value) in compound {
            column[elements.indexOf(of: key)!] = value
        }
        matrixData.append(column)
    }
        
    let matrix = Matrix<Double>.init(twoDimArr: matrixData)
    print(matrix)
    print(solve(matrix: transpose(matrix), for: zeroVectorWithSize(matrix.rows)))
    
    return nil
}

parseChemicalEquation("NO+HF2->NH3+H2O")

