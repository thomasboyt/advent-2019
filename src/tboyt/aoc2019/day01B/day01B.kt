package tboyt.aoc2019.day01B

import java.io.File

fun main() {
//    println(fuelForModule(14))
//    println(fuelForModule(1969))
//    println(fuelForModule(100756))
    val f = File("./data/day01.txt")
    val lines = f.readLines().filter { it.isNotEmpty() }
    val input = lines.map { it.toInt() }
    val total = input.fold(0) { acc, x -> acc + fuelForModule(x) }
    println(total)
}

fun fuelForModule(mass: Int): Int {
    val fuelRequired = calculateFuel(mass)
    if (fuelRequired <= 0) {
        return 0
    }
    return fuelRequired + fuelForModule(fuelRequired)
}

fun calculateFuel(mass: Int): Int {
    return (mass / 3.0).toInt() - 2
}