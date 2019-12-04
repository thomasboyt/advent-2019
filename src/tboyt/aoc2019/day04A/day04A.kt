package tboyt.aoc2019.day04A

import tboyt.aoc2019.util.expectResult

fun main() {
    expectResult(isPossiblePassword("111111"), true)
    expectResult(isPossiblePassword("223450"), false)
    expectResult(isPossiblePassword("123799"), true)
    val input = "254032-789860"
    println(findAllPasswords(input))
}

fun isPossiblePassword(password: String): Boolean {
    val digits = password.map { it.toString().toInt() }

    var hasDouble = false
    var lastDigit = digits[0]

    for (n in digits.slice(1 until digits.size)) {
        if (lastDigit > n) {
            return false
        }
        if (lastDigit == n) {
            hasDouble = true
        }
        lastDigit = n
    }

    return hasDouble
}

fun findAllPasswords(range: String): Int {
    val (min, max) = range.split('-').map { it.toInt() }
    return (min..max).filter { isPossiblePassword(it.toString()) }.size
}
