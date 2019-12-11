package tboyt.aoc2019.day09A

import tboyt.aoc2019.util.*

fun main() {
    val testProgram = parseProgram("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99")
    expectResult(createIntcodeComputer(testProgram)(listOf()).output, testProgram)
//    println(createIntcodeComputer(listOf(1102,34915192,34915192,7,4,7,99,0))(listOf()).output)
    expectResult((createIntcodeComputer(listOf(104,1125899906842624,99))(listOf()).output), listOf(1125899906842624))
    val input = parseProgram(readDataFile("day09")[0])
    println(createIntcodeComputer(input)(listOf(1L)))
}