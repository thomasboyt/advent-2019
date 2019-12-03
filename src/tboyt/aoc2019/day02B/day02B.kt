package tboyt.aoc2019.day02B

import java.io.File

fun main() {
//    println(runProgram(lineToAddresses("1,9,10,3,2,3,11,0,99,30,40,50"), 9, 10))
    val f = File("./data/day02.txt")
    val lines = f.readLines().filter { it.isNotEmpty() }
    val addresses = lineToAddresses(lines[0])
    for (noun in 0..99) {
        for (verb in 0..99) {
            val result = runProgram(addresses, noun, verb)
            if (result == 19690720) {
                println(100 * noun + verb)
                return
            }
        }
    }
}

fun lineToAddresses(line: String): List<Int> {
    return line
        .split(",") .map { it.toInt() }
}

fun runProgram(input: List<Int>, noun: Int, verb: Int): Int {
    val addresses = input.toMutableList()
    addresses[1] = noun
    addresses[2] = verb

    fun get(address: Int): Int {
        return addresses[address]
    }

    fun set(address: Int, value: Int) {
        addresses[address] = value
    }

    fun addOp(a: Int, b: Int, resultAddress: Int) {
        set(resultAddress, get(a) + get(b))
    }

    fun multOp(a: Int, b: Int, resultAddress: Int) {
        set(resultAddress, get(a) * get(b))
    }

    var instructionPointer = 0
    while (instructionPointer < addresses.size) {
        val opcode = addresses[instructionPointer]
        instructionPointer += when (opcode) {
            1 -> {
                val params = addresses.slice(instructionPointer + 1..instructionPointer + 3)
                addOp(params[0], params[1], params[2])
                4
            }
            2 -> {
                val params = addresses.slice(instructionPointer + 1..instructionPointer + 3)
                multOp(params[0], params[1], params[2])
                4
            }
            99 -> return addresses[0]
            else -> throw Exception("invalid opcode $opcode")
        }
    }

    throw Exception("did not find opcode 99")
}

