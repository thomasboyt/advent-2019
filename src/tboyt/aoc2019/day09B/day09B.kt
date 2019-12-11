package tboyt.aoc2019.day09B

import tboyt.aoc2019.util.*

fun main() {
    val input = parseProgram(readDataFile("day09")[0])
    println(createIntcodeComputer(input)(listOf(2L)))
}