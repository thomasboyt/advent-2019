package tboyt.aoc2019.day04B

import tboyt.aoc2019.util.expectResult

fun main() {
    expectResult(isPossiblePassword("112233"), true)
    expectResult(isPossiblePassword("123444"), false)
    expectResult(isPossiblePassword("111122"), true)
    val input = "254032-789860"
    println(findAllPasswords(input))
}

fun isPossiblePassword(password: String): Boolean {
    val digits = password.map { it.toString().toInt() }
    return increases(digits) && hasExclusivePair(digits)
}

fun increases(digits: List<Int>): Boolean {
    var lastDigit = digits[0]
    for (n in digits.slice(1 until digits.size)) {
        if (lastDigit > n) {
            return false
        }
        lastDigit = n
    }
    return true
}

fun hasExclusivePair(digits: List<Int>): Boolean {
    var lastDigit = digits[0]
    var lastPairDigit: Int? = null
    var inPair = false

    for (n in digits.slice(1 until digits.size)) {
        if (n == lastDigit && inPair) {
            // non-exclusive pair
            inPair = false
        } else if (n == lastDigit && n != lastPairDigit) {
            // enter pair
            inPair = true
            lastPairDigit = lastDigit
        } else if (inPair) {
            return true
        }
        lastDigit = n
    }

    return inPair
}

fun findAllPasswords(range: String): Int {
    val (min, max) = range.split('-').map { it.toInt() }
    return (min..max).filter { isPossiblePassword(it.toString()) }.size
}
