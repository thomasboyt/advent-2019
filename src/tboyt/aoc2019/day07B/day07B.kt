package tboyt.aoc2019.day07B

import tboyt.aoc2019.util.*

fun main() {
    expectResult(runSequence(listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,
            27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5), listOf(9,8,7,6,5)), 139629729)
    expectResult(runSequence(listOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
            -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
            53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10), listOf(9,7,8,5,6)), 18216)
    expectResult(findMaxThrusterSignal("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26," +
            "27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"), 139629729)
    val input = readDataFile("day07")
    println(findMaxThrusterSignal(input[0]))
}

fun findMaxThrusterSignal(input: String): Int {
    val program = parseProgram(input)
    val sequences = permute(listOf(5, 6, 7, 8, 9))
    val outputs = sequences.map { runSequence(program, it) }
    return outputs.max()!!
}

fun runSequence(program: List<Int>, sequence: List<Int>): Int {
    val computers = sequence.associateWith { createIntcodeComputer(program) }

    for (n in sequence) {
        val computer = computers[n] ?: error("computer not found")
        computer(listOf(n))
    }

    var lastOutput = listOf(0)
    var lastAmplifierEOutput = 0
    while (true) {
        for (n in sequence) {
            val computer = computers[n] ?: error("computer not found")
            val resultState = computer(lastOutput)
            lastOutput = resultState.output
            if (n == sequence.last()) {
                lastAmplifierEOutput = resultState.output[0]
                if (resultState.exitState == IntcodeExitState.HALTED) {
                    return lastAmplifierEOutput
                }
            }
        }
    }
}
