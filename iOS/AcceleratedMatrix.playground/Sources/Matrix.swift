import Accelerate

public struct Matrix<T> where T: FloatingPoint, T: ExpressibleByFloatLiteral {
    
    public var rows: Int
    public var columns: Int
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
    
    // Remove at column
    public mutating func removeColumnAt(_ targetCol: Int) -> Vector<T> {
        var col = Vector<T>()
        for r in 0..<rows {
            let index = indexForRowColumn(r, targetCol) - r
            col.append(data[index])
            data.remove(at: index)
        }
        columns -= 1
        return col
    }

    // Remove at row
    public mutating func removeRowAt(_ targetRow: Int) -> Vector<T> {
        var row = Vector<T>()
        for c in 0..<columns {
            let index = indexForRowColumn(targetRow, c) - c
            row.append(data[index])
            data.remove(at: index)
        }
        rows -= 1
        return row
    }
    
    public func getColumnAt(_ columnIndex: Int) -> Vector<T> {
        var col = Vector<T>()
        for r in 0..<rows {
             col.append(data[indexForRowColumn(r, columnIndex)])
        }
        return col
    }

    public func getRowAt(_ rowIndex: Int) -> Vector<T> {
        var row = Vector<T>()
        for c in 0..<columns {
            row.append(data[indexForRowColumn(rowIndex, c)])
        }
        return row
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

public func add(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = b
    cblas_saxpy(Int32(a.data.count), 1.0, a.data, 1, &result.data, 1)
    return result
}

public func add(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = b
    cblas_daxpy(Int32(a.data.count), 1.0, a.data, 1, &result.data, 1)
    return result
}

// MARK: Subtract

public func subtract(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = a
    cblas_saxpy(Int32(a.data.count), 1.0, negate(b.data), 1, &result.data, 1)
    return result
}

public func subtract(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = a
    cblas_daxpy(Int32(a.data.count), 1.0, negate(b.data), 1, &result.data, 1)
    return result
}

// MARK: Matrix Scalar Multiplication

public func multiply(_ scalar: Float, a: Matrix<Float>) -> Matrix<Float> {
    var result = a
    cblas_sscal(Int32(a.data.count), scalar, &result.data, 1)
    return result
}

public func multiply(_ scalar: Double, a: Matrix<Double>) -> Matrix<Double> {
    var result = a
    cblas_dscal(Int32(a.data.count), scalar, &result.data, 1)
    return result
}

// MARK: Matrix Multiplication

public func multiply(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    assert(a.columns == b.rows, "Invalid matrix dimensions for multiplication")
    var result = Matrix<Float>(repeatingValue: 0, rows: a.rows, columns: b.columns)
    cblas_sgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, Int32(a.rows), Int32(b.columns), Int32(a.columns), 1.0,
                a.data, Int32(a.columns), b.data, Int32(b.columns), 0.0, &result.data, Int32(result.columns))
    return result
}

public func multiply(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    assert(a.columns == b.rows, "Invalid matrix dimensions for multiplication")
    var result = Matrix<Double>(repeatingValue: 0, rows: a.rows, columns: b.columns)
    cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, Int32(a.rows), Int32(b.columns), Int32(a.columns), 1.0,
                a.data, Int32(a.columns), b.data, Int32(b.columns), 0.0, &result.data, Int32(result.columns))
    return result
}

// MARK: Component-wise Division

public func compDivide(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    var result = Matrix<Float>(repeatingValue: 0, rows: a.rows, columns: a.columns)
    result.data = divide(a.data, b: b.data)
    return result
}

public func compDivide(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    var result = Matrix<Double>(repeatingValue: 0, rows: a.rows, columns: a.columns)
    result.data = divide(a.data, b: b.data)
    return result
}

// MARK: Matrix division

public func divide(_ a: Matrix<Float>, b: Matrix<Float>) -> Matrix<Float> {
    return multiply(a, b: invert(b))
}

public func divide(_ a: Matrix<Double>, b: Matrix<Double>) -> Matrix<Double> {
    return multiply(a, b: invert(b))
}

// MARK: Matrix transpose

public func transpose(_ a: Matrix<Float>) -> Matrix<Float> {
    var result = Matrix<Float>(repeatingValue: 0, rows: a.columns, columns: a.rows)
    vDSP_mtrans(a.data, 1, &result.data, 1, vDSP_Length(result.rows), vDSP_Length(result.columns))
    return result
}

public func transpose(_ a: Matrix<Double>) -> Matrix<Double> {
    var result = Matrix<Double>(repeatingValue: 0, rows: a.columns, columns: a.rows)
    vDSP_mtransD(a.data, 1, &result.data, 1, vDSP_Length(result.rows), vDSP_Length(result.columns))
    return result
}

// MARK: Matrix inversion

public func invert(_ a: Matrix<Float>) -> Matrix<Float> {
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

public func invert(_ a: Matrix<Double>) -> Matrix<Double> {
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

public func solve(matrix: Matrix<Double>, for vector: Vector<Double>) -> Vector<Double> {
    let A: [Double] = matrix.data
    let matA = la_matrix_from_double_buffer(A, la_count_t(matrix.rows), la_count_t(matrix.columns), la_count_t(matrix.columns), la_hint_t(LA_NO_HINT), la_attribute_t(LA_DEFAULT_ATTRIBUTES))
    
    let b: [Double] = vector
    let vecB = la_matrix_from_double_buffer(b, la_count_t(vector.count), 1, 1, la_hint_t(LA_NO_HINT), la_attribute_t(LA_DEFAULT_ATTRIBUTES))
    
    let vecCj = la_solve(matA, vecB)
    var cj: [Double] = Array(repeating: 0.0, count: vector.count)
    
    let status = la_matrix_to_double_buffer(&cj, 1, vecCj)
    if status == la_status_t(LA_SUCCESS) {
        return cj
    } else {
        return Vector<Double>()
    }
}

//public func solve(matrix: Matrix<Double>, for vector: Vector<Double>) -> Vector<Double> {
//    var systemMatrix = matrix.data
//    var solution = vector
//
//    var n = __CLPK_integer(sqrt(Float(matrix.data.count)))
//
//    var pivots = [__CLPK_integer](repeating: 0, count: Int(n))
//
//    var solutionCols: __CLPK_integer = 1
//    var systemLeadingDimensions = n
//    var solutionLeadingDimensions = n
//    var error: __CLPK_integer = 0
//
//    withUnsafeMutablePointer(to: &n) {
//        dgetrf_($0, $0, &systemMatrix, $0, &pivots, &error)
//    }
//
//    _ = "T".withCString {
//        dgetrs_(UnsafeMutablePointer(mutating: $0), &n, &solutionCols, &systemMatrix, &systemLeadingDimensions, &pivots, &solution, &solutionLeadingDimensions, &error)
//    }
//
//    print(systemMatrix)
//
//    return systemMatrix
//}

extension Matrix: CustomStringConvertible {
    public var description: String {
        var description = ""
        
        for r in 0..<rows {
            description += (0..<columns).map({ "\(self[r, $0])" }).joined(separator: " ") + "\n"
        }
        
        return description
    }
}
