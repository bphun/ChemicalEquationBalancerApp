//
//  ChemicalEquation.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 1/25/20.
//  Copyright © 2020 Brandon Phan. All rights reserved.
//

import Foundation

public class ChemicalEquation {

    private var equationStr: String
    private var elementSet: Set<String>
    private var equationMatrix: Matrix<Double>
    private var unProcessedSolution: Vector<Double>
    private var lhsElements: [[String : Double]]
    private var rhsElements: [[String : Double]]
    
    public init(equationStr: String) {
        self.equationStr = equationStr
        self.elementSet = Set<String>()
        self.equationMatrix = Matrix<Double>()
        self.unProcessedSolution = Vector<Double>()
        self.lhsElements = [[String : Double]]()
        self.rhsElements = [[String : Double]]()
    }
    
    public func balance() -> Vector<Double>? {
        guard isValidChemicalEquation() else { return nil }
        
        let equationSides = equationStr.components(separatedBy: "->")
        
        lhsElements = parseEquationSide(equationSide: equationSides[0])
        rhsElements = parseEquationSide(equationSide: equationSides[1])
        elementSet = getUniqueElements(lhsElements).union(getUniqueElements(rhsElements))
        
//        print(elementSet)
        
        var matrixData = [[Double]]()
        
        loadMatrixData(matrixData: &matrixData, equation: lhsElements, elementIndexMapping: elementSet, isProductSide: false)
        loadMatrixData(matrixData: &matrixData, equation: rhsElements, elementIndexMapping: elementSet, isProductSide: true)
        
        equationMatrix = transpose(Matrix<Double>.init(twoDimArr: matrixData))
//        print(transposedMatrix)
        let vector = equationMatrix.removeColumnAt(equationMatrix.columns - 1)
        var solution = solve(matrix: equationMatrix, for: vector)
//        print(solution)
        unProcessedSolution = solution
        solution = convertToWholeNumbers(vector: solution)
        
        return solution
    }
    
    public func matrix() -> Matrix<Double> {
        return self.equationMatrix
    }
    
    public func elements() -> Set<String> {
        return self.elementSet
    }
    
    public func getUnProcessedSolution() -> Vector<Double> {
        return self.unProcessedSolution
    }
    
    public func getLhsElements() -> [[String : Double]] {
        return self.lhsElements
    }
    
    public func getRhsElements() -> [[String : Double]] {
        return self.rhsElements
    }
    
    private func parseEquationSide(equationSide: String) -> [[String : Double]] {
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

    private func getUniqueElements(_ compounds: [[String : Double]]) -> Set<String> {
        var elements = Set<String>()
        for compound in compounds {
            compound.keys.forEach {elements.insert($0) }
        }
        return elements
    }

    private func loadMatrixData(matrixData: inout [[Double]], equation: [[String : Double]], elementIndexMapping: Set<String>, isProductSide: Bool) {
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
    private func convertToWholeNumbers(vector: Vector<Double>) -> Vector<Double> {
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
    
    private func isValidChemicalEquation() -> Bool {
        return true
    }
}

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
