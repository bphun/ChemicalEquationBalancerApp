//
//  Vector.swift
//  ChemicalEquationBalancerApp
//
//  Created by Brandon Phan on 12/15/19.
//  Copyright © 2019 Brandon Phan. All rights reserved.
//

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

internal func sum(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_sve(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func sum(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_sveD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Absolute Sum

internal func absoluteSum(_ a: Vector<Float>) -> Float {
    return cblas_sasum(Int32(a.count), a, 1)
}

internal func absoluteSum(_ a: Vector<Double>) -> Double {
    return cblas_dasum(Int32(a.count), a, 1)
}

// MARK: Min Value

internal func min(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_minv(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func min(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_minvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Max Value

internal func max(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_maxv(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func max(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_maxvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Min Value Index

internal func minIndex(_ a: Vector<Float>) -> Int {
    var value: Float = 0.0
    var index: vDSP_Length = 0
    vDSP_minvi(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

internal func minIndex(_ a: Vector<Double>) -> Int {
    var value: Double = 0.0
    var index: vDSP_Length = 0
    vDSP_minviD(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

// MARK: Max Value Index

internal func maxIndex(_ a: Vector<Float>) -> Int {
    var value: Float = 0.0
    var index: vDSP_Length = 0
    vDSP_maxvi(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

internal func maxIndex(_ a: Vector<Double>) -> Int {
    var value: Double = 0.0
    var index: vDSP_Length = 0
    vDSP_maxviD(a, 1, &value, &index, vDSP_Length(a.count))
    return Int(index)
}

// MARK: Mean Value

internal func mean(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_meanv(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func mean(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_meanvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Mean Magnitude of Values

internal func meanMagnitude(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_meamgv(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func meanMagnitude(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_meamgvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Mean Square of Values

internal func meanSquare(_ a: Vector<Float>) -> Float {
    var result: Float = 0.0
    vDSP_measqv(a, 1, &result, vDSP_Length(a.count))
    return result
}

internal func meanSquare(_ a: Vector<Double>) -> Double {
    var result: Double = 0.0
    vDSP_measqvD(a, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Vector Addition

internal func add(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(b)
    cblas_saxpy(Int32(a.count), 1.0, a, 1, &result, 1)
    return result
}

internal func add(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(b)
    cblas_daxpy(Int32(a.count), 1.0, a, 1, &result, 1)
    return result
}

// MARK: Vector Subtraction

internal func sub(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(b)
    catlas_saxpby(Int32(a.count), 1.0, a, 1, -1, &result, 1)
    return result
}

internal func sub(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(b)
    catlas_daxpby(Int32(a.count), 1.0, a, 1, -1, &result, 1)
    return result
}


// MARK: Vector Multiplication

internal func multiply(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vmul(a, 1, b, 1, &result, 1, vDSP_Length(a.count))
    return result
}

internal func multiply(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vmulD(a, 1, b, 1, &result, 1, vDSP_Length(a.count))
    return result
}

// MARK: Vector Division

internal func divide(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvdivf(&result, a, b, [Int32(a.count)])
    return result
}

internal func divide(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvdiv(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Component wise Modulo

internal func mod(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvfmodf(&result, a, b, [Int32(a.count)])
    return result
}

internal func mod(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvfmod(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Component wise Remainder

internal func remainder(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvremainderf(&result, a, b, [Int32(a.count)])
    return result
}

internal func remainder(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvremainder(&result, a, b, [Int32(a.count)])
    return result
}

// MARK: Square root

internal func sqrt(_ a: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvsqrtf(&result, a, [Int32(a.count)])
    return result
}

internal func sqrt(_ a: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvsqrt(&result, a, [Int32(a.count)])
    return result
}

// MARK: Dot Product

internal func dot(_ a: Vector<Float>, b: Vector<Float>) -> Float {
    assert(a.count == b.count)
    
    var result: Float = 0.0
    vDSP_dotpr(a, 1, b, 1, &result, vDSP_Length(a.count))
    return result
}

internal func dot(_ a: Vector<Double>, b: Vector<Double>) -> Double {
    assert(a.count == b.count)
    
    var result: Double = 0.0
    vDSP_dotprD(a, 1, b, 1, &result, vDSP_Length(a.count))
    return result
}

// MARK: Scalar distance between two vectors

internal func distance(_ a: Vector<Float>, b: Vector<Float>) -> Float {
    assert(a.count == b.count)
    
    let subtract = sub(a, b: b)
    var squared = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vsq(subtract, 1, &squared, 1, vDSP_Length(a.count))
    
    return sqrt(sum(squared))
}

internal func distance(_ a: Vector<Double>, b: Vector<Double>) -> Double {
    assert(a.count == b.count)
    
    let subtract = sub(a, b: b)
    var squared = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vsqD(subtract, 1, &squared, 1, vDSP_Length(a.count))
    
    return sqrt(sum(squared))
}

// MARK: Power

internal func pow(_ a: Vector<Float>, b: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vvpowf(&results, a, b, [Int32(a.count)])

    return results
}

internal func pow(_ a: Vector<Double>, b: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vvpow(&results, a, b, [Int32(a.count)])

    return results
}

internal func pow(_ a: Vector<Float>, b: Float) -> Vector<Float> {
    let bVec = Vector<Float>(repeating: b, count: a.count)
    return pow(bVec, b: a)
}

internal func pow(_ a: Vector<Double>, b: Double) -> Vector<Double> {
    let bVec = Vector<Double>(repeating: b, count: a.count)
    return pow(bVec, b: a)
}

// MARK: Exponentiation

internal func exp(_ a: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vvexpf(&results, a, [Int32(a.count)])
    return results
}

internal func exp(_ a: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vvexp(&results, a, [Int32(a.count)])
    return results
}

// MARK: Copy Sign

internal func copysign(sign: Vector<Float>, magnitude: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: sign.count)
    vvcopysignf(&results, magnitude, sign, [Int32(sign.count)])

    return results
}

internal func copysign(sign: Vector<Double>, magnitude: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: sign.count)
    vvcopysign(&results, magnitude, sign, [Int32(sign.count)])

    return results
}

// MARK: Negate

internal func negate(_ a: Vector<Float>) -> Vector<Float> {
    var results = Vector<Float>(repeating: 0.0, count: a.count)
    vDSP_vneg(a, 1, &results, 1, vDSP_Length(a.count))

    return results
}

internal func negate(_ a: Vector<Double>) -> Vector<Double> {
    var results = Vector<Double>(repeating: 0.0, count: a.count)
    vDSP_vnegD(a, 1, &results, 1, vDSP_Length(a.count))

    return results
}

// MARK: Reciprocal

internal func reciprocal(_ a: Vector<Float>) -> Vector<Float> {
    var result = Vector<Float>(repeating: 0.0, count: a.count)
    vvrecf(&result, a, [Int32(a.count)])

    return result
}

internal func reciprocal(_ a: Vector<Double>) -> Vector<Double> {
    var result = Vector<Double>(repeating: 0.0, count: a.count)
    vvrec(&result, a, [Int32(a.count)])

    return result
}

