package tboyt.aoc2019.day07A

import tboyt.aoc2019.util.createIntcodeComputer
import tboyt.aoc2019.util.expectResult
import tboyt.aoc2019.util.parseProgram
import tboyt.aoc2019.util.readDataFile
import tboyt.aoc2019.util.permute

fun main() {
    expectResult(runSequence(listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0), listOf(4,3,2,1,0)), 43210)
    expectResult(findMaxThrusterSignal("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"), 43210)
    val input = readDataFile("day07")
    println(findMaxThrusterSignal(input[0]))
}

fun findMaxThrusterSignal(input: String): Int {
    val program = parseProgram(input)
    val sequences = permute(listOf(0, 1, 2, 3, 4))
    val outputs = sequences.map { runSequence(program, it) }
    return outputs.max()!!
}

fun runSequence(program: List<Int>, sequence: List<Int>): Int {
    var lastOutput = 0
    for (n in sequence) {
        lastOutput = createIntcodeComputer(program)(listOf(n, lastOutput)).output[0]
    }
    return lastOutput
}
