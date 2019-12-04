package tboyt.aoc2019.util

import java.io.File

fun readDataFile(name: String): List<String> {
    val f = File("./data/$name.txt")
    return f.readLines().filter { it.isNotEmpty() }
}

fun <T>expectResult(actual: T, expected: T) {
    if (actual != expected) {
        throw Exception("expected $expected to be $actual")
    }
}