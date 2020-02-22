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

func loadMatrixData(matrixData: inout [[Double]], equation: [[String : Double]], elementIndexMapping: Set<String>, isProductSide: Bool) {
    for compound in equation {
        var column = [Double](repeating: 0, count: elementIndexMapping.count)
        for (key, value) in compound {
            column[elementIndexMapping.indexOf(of: key)!] = isProductSide ? -value : value
        }
        matrixData.append(column)
    }
}

/*
 NOTE: Floating point precison error is high with NO+H2->NH3+H2O equation. Errors tend to occur when
 elements set=["O", "H", "N"]([-1.0, -2.5, -0.9999999999999999]), ["N", "H", "O"]([-0.9999999999999999, -2.5, -0.9999999999999999]), ["H", "N", "O"], ["H", "O", "N"]([-2.5, -0.9999999999999999, -1.0])
 Success when elements set=["O", "N", "H"]([-1.0, -2.5, -1.0]),["N", "O", "H"]([-1.0, -2.5, -1.0]), ["O", "N", "H"]([-1.0, -2.5, -1.0])
*/
func convertToWholeNumbers(vector: Vector<Double>) -> Vector<Double> {
    var multiplier: Double = 1
    while true {
        var multipliedSolution = vector.map({ ($0 < 0 ? -$0 : $0) * multiplier })
        for (index, value) in multipliedSolution.enumerated() {
//            print("Floor: \(floor(value)) value: \(value) finite: \(value.isFinite)")
            if (value == floor(value)) && value.isFinite {
//                print("index: \(index) count: \(vector.count)")
                if index == vector.count - 1 {
                    multipliedSolution.append(multiplier)
                    return multipliedSolution
                }
            } else {
                multiplier += 1
                break
            }
        }
    }
}

func balanceChemicalEquation(_ input: String?) -> Vector<Double>? {
    guard let input = input else { return nil }
    guard isValidChemicalEquation(input) else { return nil }
    
    let equationSides = input.components(separatedBy: "->")
    
    let lhs = parseEquationSide(equationSide: equationSides[0])
    let rhs = parseEquationSide(equationSide: equationSides[1])
    let elements = getUniqueElements(lhs).union(getUniqueElements(rhs))
    
    print(elements)
    
    var matrixData = [[Double]]()
    
    loadMatrixData(matrixData: &matrixData, equation: lhs, elementIndexMapping: elements, isProductSide: false)
    loadMatrixData(matrixData: &matrixData, equation: rhs, elementIndexMapping: elements, isProductSide: true)
    
    let matrix = Matrix<Double>.init(twoDimArr: matrixData)
    var transposedMatrix = transpose(matrix)
    print(transposedMatrix)
    let vector = transposedMatrix.removeColumnAt(transposedMatrix.columns - 1)
    var solution = solve(matrix: transposedMatrix, for: vector)
    print(solution)
    solution = convertToWholeNumbers(vector: solution)
    
    return solution
}

//print(balanceChemicalEquation("NO+H2->NH3+H2O")!)
print(balanceChemicalEquation("NH4Cl+NaOH->NH4OH+NaCl")!)
//print("CH4+O2->CO2+H2O".addingPercentEncoding(withAllowedCharacters: .alphanumerics))

//    for compound in lhs {
//        var column = [Double](repeating: 0, count: rows)
//        for (key, value) in compound {
//            column[elements.indexOf(of: key)!] = value
//        }
//        matrixData.append(column)
//    }
//
//    for compound in rhs {
//        var column = [Double](repeating: 0, count: rows)
//        for (key, value) in compound {
//            column[elements.indexOf(of: key)!] = -value
//        }
//        matrixData.append(column)
//    }
    

//import Foundation
//import Accelerate.vecLib.LinearAlgebra

//let A: [Double] = [
//    4.0, 0.0, 0.0,
//    1.0, 0.0, -1.0,
//    0.0, 2.0, -2.0
//]
//let matA = la_matrix_from_double_buffer(A, 3, 3, 3, la_hint_t(LA_NO_HINT), la_attribute_t(LA_DEFAULT_ATTRIBUTES))
//
//let b: [Double] = [-2.0, 0, -1]
//let vecB = la_matrix_from_double_buffer(b, 3, 1, 1, la_hint_t(LA_NO_HINT), la_attribute_t(LA_DEFAULT_ATTRIBUTES))
//
//let vecCj = la_solve(matA, vecB)
//var cj: [Double] = Array(repeating: 0.0, count: 3)
//
//let status = la_matrix_to_double_buffer(&cj, 1, vecCj)
//if status == la_status_t(LA_SUCCESS) {
//    print(cj)
//} else {
//    print("Failure: \(status)")
//}

var A: [Double] = [
    4.0, 0.0, 0.0, -2,
    1.0, 0.0, -1.0, 0,
    0.0, 2.0, -2.0, -1
]

//print(Matrix<Double>(A, rows: 3, columns: 4));
//
let rows = 3
let columns = 4
let targetCol = 1
let targetRow = 1

func indexForRowColumn(_ row: Int, _ column: Int) -> Int {
    return (row * columns) + column
}

// Remove at column
for r in 0..<rows {
    let index = indexForRowColumn(r, targetCol)
    A.remove(at: index - r)
}
print(Matrix<Double>(A, rows: 3, columns: 3))

// Remove at row
//for c in 0..<columns {
//    let index = indexForRowColumn(targetRow, c)
//    A.remove(at: index - c)
//}

// Get column
//var col = [Double]()
//for r in 0..<rows {
//    let index = indexForRowColumn(r, targetCol)
//    col.append(A[index])
//}
//print(col)

//var row = [Double]()
//for c in 0..<columns {
//    let index = indexForRowColumn(targetRow, c)
//    row.append(A[index])
//}
//print(row)

//print(Matrix<Double>(A, rows: 2, columns: 4))
