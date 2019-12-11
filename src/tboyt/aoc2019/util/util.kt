package tboyt.aoc2019.util

import java.io.File

fun readDataFile(name: String): List<String> {
    val f = File("./data/$name.txt")
    return f.readLines().filter { it.isNotEmpty() }
}

fun <T>expectResult(actual: T, expected: T) {
    if (actual != expected) {
        throw Exception("expected $actual to be $expected")
    }
}

// https://rosettacode.org/wiki/Permutations#Kotlin
fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}
