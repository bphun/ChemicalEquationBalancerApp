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

let system: [[Double]] = [
    [1, 0, 1, 4],
    [0, 0, 1, 2]
]
let augmentVector: Vector<Double> = [1, 0]

let systemMatrix = Matrix<Double>(twoDimArr: system)

//print(invert(systemMatrix))
print("Matrix:\n\(systemMatrix)")
print("Augmented column: \(augmentVector)")

print("Solution: \(solve(matrix: systemMatrix, for: augmentVector))")
