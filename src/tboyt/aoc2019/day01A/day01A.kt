package tboyt.aoc2019.day01A

import java.io.File

fun main() {
//    println(calculateFuel(12))
//    println(calculateFuel(14))
//    println(calculateFuel(1969))
//    println(calculateFuel(100756))
    val f = File("./data/day01.txt")
    val lines = f.readLines().filter { it.isNotEmpty() }
    val input = lines.map { it.toInt() }
    val total = input.fold(0) { acc, x -> acc + calculateFuel(x) }
    println(total)
}

fun calculateFuel(mass: Int): Int {
    return (mass / 3.0).toInt() - 2
}