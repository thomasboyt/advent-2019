package tboyt.aoc2019.day03B

import tboyt.aoc2019.util.expectResult
import tboyt.aoc2019.util.readDataFile
import kotlin.math.abs

val testA = arrayOf("R8,U5,L5,D3", "U7,R6,D4,L4")
val testB = arrayOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
val testC = arrayOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

fun main() {
    val input = readDataFile("day03")
    expectResult(closestIntersection(testA[0], testA[1]), 30)
    expectResult(closestIntersection(testB[0], testB[1]), 610)
    expectResult(closestIntersection(testC[0], testC[1]), 410)
    println(closestIntersection(input[0], input[1]))
}

fun closestIntersection(wireA: String, wireB: String): Int? {
    val wireAPoints = mutableMapOf<Pair<Int, Int>, Int>()
    val intersectionPointSteps = mutableListOf<Int>()

    forEachWirePoint(wireA) { x, y, steps ->
        wireAPoints[Pair(x, y)] = steps
    }

    forEachWirePoint(wireB) { x, y, steps ->
        val wireASteps = wireAPoints[Pair(x, y)]
        if (wireASteps != null) {
            intersectionPointSteps.add(wireASteps + steps)
        }
    }

    return intersectionPointSteps.min()
}

fun unitVectorForDirection(direction: Char): Pair<Int, Int> {
    return when (direction) {
        'U' -> Pair(0, -1)
        'D' -> Pair(0, 1)
        'L' -> Pair(-1, 0)
        'R' -> Pair(1, 0)
        else -> throw Exception("Unrecognized direction $direction")
    }
}

fun forEachWirePoint(wire: String, fn: (x: Int, y: Int, steps: Int) -> Unit) {
    val wireInstructions = wire.split(",")

    var cx = 0
    var cy = 0
    var cSteps = 0

    for (instruction in wireInstructions) {
        val direction = instruction[0]
        val vec = unitVectorForDirection(direction)
        val distance = instruction.substring(1).toInt()
        for (n in 1..distance) {
            cx += vec.first
            cy += vec.second
            cSteps += 1
            fn(cx, cy, cSteps)
        }
    }
}
