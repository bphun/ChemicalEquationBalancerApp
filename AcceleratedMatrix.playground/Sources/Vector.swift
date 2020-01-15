import Accelerate

public typealias Vector<FloatingPoint> = [FloatingPoint]

extension Vector where Element: Strideable {
    init(from: Element, to: Element, by: Element.Stride) {
        self.init(stride(from: from, to: to, by: by))
    }
    
    init(from: Element, through: Element, by: Element.Stride) {
        self.init(stride(from: from, through: through, by: by))
    }
    
    init(initializedWith initializer: () -> Element, count: Int) {
        self.init(Vector((0..<count).map { _ in initializer() }))
    }
}

// MARK: Sum

public func sum(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_sve(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func sum(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_sveD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Absolute Sum

public func absoluteSum(_ a: Vector<Float>) -> Float {
    return cblas_sasum(Int32(a.count), a, 1)
}

public func absoluteSum(_ a: Vector<Double>) -> Double {
    return cblas_dasum(Int32(a.count), a, 1)
}

// MARK: Min Value

public func min(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_minv(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func min(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_minvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Max Value

public func max(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_maxv(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func max(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_maxvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Min Value Index

public func minIndex(_ a: Vector<Float>) -> Int {
    var value: Float = 0.0
    var index: vDSP_Length = 0
    vDSP_minvi(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

public func minIndex(_ a: Vector<Double>) -> Int {
    var value: Double = 0.0
    var index: vDSP_Length = 0
    vDSP_minviD(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

// MARK: Max Value Index

public func maxIndex(_ a: Vector<Float>) -> Int {
    var value: Float = 0.0
    var index: vDSP_Length = 0
    vDSP_maxvi(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

public func maxIndex(_ a: Vector<Double>) -> Int {
    var value: Double = 0.0
    var index: vDSP_Length = 0
    vDSP_maxviD(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

// MARK: Mean Value

public func mean(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_meanv(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func mean(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_meanvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Mean Magnitude of Values

public func meanMagnitude(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_meamgv(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func meanMagnitude(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_meamgvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Mean Square of Values

public func meanSquare(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_measqv(a, 1, &result, vDSP_Length(a.count))
    return result
}

public func meanSquare(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_measqvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Vector Addition

public func add(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(b)
    cblas_saxpy(Int32(a.count), 1.0, a, 1, &result, 1)
    return result
}

public func add(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(b)
    cblas_daxpy(Int32(a.count), 1.0, a, 1, &result, 1)
    return result
}

// MARK: Vector Subtraction

public func sub(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(b)
    catlas_saxpby(Int32(a.count), 1.0, a, 1, -1, &result, 1)
    return result
}

public func sub(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(b)
    catlas_daxpby(Int32(a.count), 1.0, a, 1, -1, &result, 1)
    return result
}


// MARK: Vector Multiplication

public func multiply(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vmul(a, 1, b, 1, &result, 1, vDSP_Length(a.count))
    return result
}

public func multiply(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vmulD(a, 1, b, 1, &result, 1, vDSP_Length(a.count))
    return result
}

// MARK: Vector Division

public func divide(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvdivf(&result, a, b, [Int32(a.count)])
    return result
}

public func divide(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvdiv(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Component wise Modulo

public func mod(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvfmodf(&result, a, b, [Int32(a.count)])
    return result
}

public func mod(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvfmod(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Component wise Remainder

public func remainder(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvremainderf(&result, a, b, [Int32(a.count)])
    return result
}

public func remainder(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvremainder(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Square root

public func sqrt(_ a: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvsqrtf(&result, a, [Int32(a.count)])
    return result
}

public func sqrt(_ a: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvsqrt(&result, a, [Int32(a.count)])
    return result
}

// MARK: Dot Product

public func dot(_ a: Vector<Float>, b: Vector<Float>) -> Float {
    assert(a.count == b.count)
    
    var result: Float = 0.0
    vDSP_dotpr(a, 1, b, 1, &result, vDSP_Length(a.count))
    return result
}

public func dot(_ a: Vector<Double>, b: Vector<Double>) -> Double {
    assert(a.count == b.count)
    
    var result: Double = 0.0
    vDSP_dotprD(a, 1, b, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Scalar distance between two vectors

public func distance(_ a: Vector<Float>, b: Vector<Float>) -> Float {
    assert(a.count == b.count)
    
    let subtract = sub(a, b: b)
    var squared = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vsq(subtract, 1, &squared, 1, vDSP_Length(a.count))
    
    return sqrt(sum(squared))
}

public func distance(_ a: Vector<Double>, b: Vector<Double>) -> Double {
    assert(a.count == b.count)
    
    let subtract = sub(a, b: b)
    var squared = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vsqD(subtract, 1, &squared, 1, vDSP_Length(a.count))
    
    return sqrt(sum(squared))
}

// MARK: Power

public func pow(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vvpowf(&results, a, b, [Int32(a.count)])

    return results
}

public func pow(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vvpow(&results, a, b, [Int32(a.count)])

    return results
}

public func pow(_ a: Vector<Float>, b: Float) -> Vector<Float> {
    let bVec = Vector<Float>(repeating: b, count: a.count)
    return pow(bVec, b: a)
}

public func pow(_ a: Vector<Double>, b: Double) -> Vector<Double> {
    let bVec = Vector<Double>(repeating: b, count: a.count)
    return pow(bVec, b: a)
}

// MARK: Exponentiation

public func exp(_ a: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vvexpf(&results, a, [Int32(a.count)])
    return results
}

public func exp(_ a: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vvexp(&results, a, [Int32(a.count)])
    return results
}

// MARK: Copy Sign

public func copysign(sign: Vector<Float>, magnitude: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: sign.count)
    vvcopysignf(&results, magnitude, sign, [Int32(sign.count)])

    return results
}

public func copysign(sign: Vector<Double>, magnitude: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: sign.count)
    vvcopysign(&results, magnitude, sign, [Int32(sign.count)])

    return results
}

// MARK: Negate

public func negate(_ a: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vneg(a, 1, &results, 1, vDSP_Length(a.count))

    return results
}

public func negate(_ a: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vnegD(a, 1, &results, 1, vDSP_Length(a.count))

    return results
}

// MARK: Reciprocal

public func reciprocal(_ a: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvrecf(&result, a, [Int32(a.count)])

    return result
}

public func reciprocal(_ a: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvrec(&result, a, [Int32(a.count)])

    return result
}
