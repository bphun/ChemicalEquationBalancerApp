//
//  Matrix.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/15/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

import Accelerate

internal struct Matrix<T> where T: FloatingPoint, T: ExpressibleByFloatLiteral {
    
    let rows: Int
    let columns: Int
    internal var data: Vector<T>
    
    public init(_ data: [T], rows: Int, columns: Int) {
        assert(rows * columns == data.count)
        
        self.rows = rows
        self.columns = columns
        self.data = data
    }
    
    public init(repeatingValue: T, rows: Int, columns: Int) {
        self.init(Vector<T>(repeating: repeatingValue, count: rows * columns), rows: rows, columns: columns)
    }
    
    public init(copy: Matrix) {
        self.init(copy.data, rows: copy.rows, columns: copy.columns)
    }
    
    public init(twoDimArr contents: [[T]]) {
        let rows = contents.count
        let flattened = contents.flatMap({ $0 })
        let columns = flattened.count / rows
        self.init(flattened, rows: rows, columns: columns)
    }
    
    public subscript(row: Int, column: Int) -> T {
        get {
            return data[indexForRowColumn(row, column)]
        }
        set {
            data[indexForRowColumn(row, column)] = newValue
        }
    }
    
    public subscript(row row: Int) -> [T] {
        get {
            assert(row < rows)
            return Array(data[indexForRowColumn(row, 0)...indexForRowColumn(row, columns - 1)])
        }
        set {
            assert(row < rows)
            assert(newValue.count == columns)
            data.replaceSubrange(indexForRowColumn(row, 0)...indexForRowColumn(row, columns - 1), with: newValue)
        }
    }
    
    public subscript(column column: Int) -> [T] {
        get {
            assert(column < columns)
            let strides = stride(from: indexForRowColumn(0, column), to: data.count, by: columns)
            return strides.map { data[$0] }
        }
        set {
            assert(column < columns)
            assert(newValue.count == rows)
            let strides = stride(from: indexForRowColumn(0, column), to: data.count, by: columns)
            strides.enumerated().forEach { data[$1] = newValue[$0] }
        }
    }
    
    private func indexIsValidForRow(_ row: Int, column: Int) -> Bool {
        return row >= 0 && row < rows && column >= 0 && column < columns
    }
    
    private func indexForRowColumn(_ row: Int, _ column: Int) -> Int {
        assert(indexIsValidForRow(row, column: column))
        return (row * columns) + column
    }
}

// MARK: Addition

internal func add(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = b
    cblas_saxpy(Int32(a.data.count), 1.0, a.data, 1, &result.data, 1)
    return result
}

internal func add(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = b
    cblas_daxpy(Int32(a.data.count), 1.0, a.data, 1, &result.data, 1)
    return result
}

// MARK: Subtract

internal func subtract(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = a
    cblas_saxpy(Int32(a.data.count), 1.0, negate(b.data), 1, &result.data, 1)
    return result
}

internal func subtract(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = a
    cblas_daxpy(Int32(a.data.count), 1.0, negate(b.data), 1, &result.data, 1)
    return result
}

// MARK: Matrix Scalar Multiplication

internal func multiply(_ scalar: Float, a: Matrix<Float>) -> Matrix<Float> {
    var result = a
    cblas_sscal(Int32(a.data.count), scalar, &result.data, 1)
    return result
}

internal func multiply(_ scalar: Double, a: Matrix<Double>) -> Matrix<Double> {
    var result = a
    cblas_dscal(Int32(a.data.count), scalar, &result.data, 1)
    return result
}

// MARK: Matrix Multiplication

internal func multiply(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    assert(a.columns == b.rows, "Invalid matrix dimensions for multiplication")
    var result = Matrix<Float>(repeatingValue: 0, rows: a.rows, columns: b.columns)
    cblas_sgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, Int32(a.rows), Int32(b.columns), Int32(a.columns), 1.0,
                a.data, Int32(a.columns), b.data, Int32(b.columns), 0.0, &result.data, Int32(result.columns))
    return result
}

internal func multiply(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    assert(a.columns == b.rows, "Invalid matrix dimensions for multiplication")
    var result = Matrix<Double>(repeatingValue: 0, rows: a.rows, columns: b.columns)
    cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, Int32(a.rows), Int32(b.columns), Int32(a.columns), 1.0,
                a.data, Int32(a.columns), b.data, Int32(b.columns), 0.0, &result.data, Int32(result.columns))
    return result
}

// MARK: Component-wise Division

internal func compDivide(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = Matrix<Float>(repeatingValue: 0, rows: a.rows, columns: a.columns)
    result.data = divide(a.data, b: b.data)
    return result
}

internal func compDivide(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = Matrix<Double>(repeatingValue: 0, rows: a.rows, columns: a.columns)
    result.data = divide(a.data, b: b.data)
    return result
}

// MARK: Matrix division

internal func divide(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    return multiply(a, b: invert(b))
}

internal func divide(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    return multiply(a, b: invert(b))
}

// MARK: Matrix transpose

internal func transpose(_ a: Matrix<Float>) -> Matrix<Float> {
    var result = Matrix<Float>(repeatingValue: 0, rows: a.columns, columns: a.rows)
    vDSP_mtrans(a.data, 1, &result.data, 1, vDSP_Length(result.rows), vDSP_Length(result.columns))
    return result
}

internal func transpose(_ a: Matrix<Double>) -> Matrix<Double> {
    var result = Matrix<Double>(repeatingValue: 0, rows: a.columns, columns: a.rows)
    vDSP_mtransD(a.data, 1, &result.data, 1, vDSP_Length(result.rows), vDSP_Length(result.columns))
    return result
}

// MARK: Matrix inversion

internal func invert(_ a: Matrix<Float>) -> Matrix<Float> {
    var result = a
    var n = __CLPK_integer(sqrt(Float(a.data.count)))
    var pivots = [__CLPK_integer](repeating: 0, count: Int(n))
    var workspace = [Float](repeating: 0.0, count: Int(n))
    var error : __CLPK_integer = 0
    
    withUnsafeMutablePointer(to: &n) {
        sgetrf_($0, $0, &result.data, $0, &pivots, &error)
        sgetri_($0, &result.data, $0, &pivots, &workspace, $0, &error)
    }
    
    return result
}

internal func invert(_ a: Matrix<Double>) -> Matrix<Double> {
    var result = a
    var n = __CLPK_integer(a.data.count)
    var pivots = [__CLPK_integer](repeating: 0, count: Int(n))
    var workspace = [Double](repeating: 0.0, count: Int(n))
    var error: __CLPK_integer = 0
    
    withUnsafeMutablePointer(to: &n) {
        dgetrf_($0, $0, &result.data, $0, &pivots, &error)
        dgetri_($0, &result.data, $0, &pivots, &workspace, $0, &error)
    }
    
    return result
}

internal func solve(matrix: Matrix<Double>, for vector: Vector<Double>) -> Vector<Double> {
    var systemMatrix = matrix.data
    var solution = vector
    
    var n = __CLPK_integer(sqrt(Float(matrix.data.count)))

    var pivots = [__CLPK_integer](repeating: 0, count: Int(n))
    
    var solutionCols: __CLPK_integer = 1
    var systemLeadingDimensions = n
    var solutionLeadingDimensions = n
    var error: __CLPK_integer = 0

    print(error)
    
    withUnsafeMutablePointer(to: &n) {
        dgetrf_($0, $0, &systemMatrix, $0, &pivots, &error)
    }
    
    print(error)
    
    _ = "T".withCString {
        dgetrs_(UnsafeMutablePointer(mutating: $0), &n, &solutionCols, &systemMatrix, &systemLeadingDimensions, &pivots, &solution, &solutionLeadingDimensions, &error)
    }
    
    print(error)
    
    return solution
}

extension Matrix: CustomStringConvertible {
    public var description: String {
        var description = ""
        
        for r in 0..<rows {
            description += (0..<columns).map({ "\(self[r, $0])" }).joined(separator: " ") + "\n"
        }
        
        return description
    }
}
