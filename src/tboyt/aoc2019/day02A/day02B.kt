package tboyt.aoc2019.day02A

import java.io.File

fun main() {
//    println(runProgram("1,9,10,3,2,3,11,0,99,30,40,50"))
    val f = File("./data/day02.txt")
    val lines = f.readLines().filter { it.isNotEmpty() }
    val registers = lineToRegisters(lines[0])
    registers[1] = 12
    registers[2] = 2
    println(runProgram(registers))
}

fun lineToRegisters(line: String): MutableList<Int> {
    return line
        .split(",") .map { it.toInt() }
        .toMutableList()
}

fun runProgram(registers: MutableList<Int>): Int {
    fun addOp(idx: Int) {
        registers[registers[idx + 3]] = registers[registers[idx + 1]] + registers[registers[idx + 2]]
    }

    fun multOp(idx: Int) {
        registers[registers[idx + 3]] = registers[registers[idx + 1]] * registers[registers[idx + 2]]
    }

    for (idx in 0..registers.size step 4) {
       val opcode = registers[idx]
        when (opcode) {
            1 -> addOp(idx)
            2 -> multOp(idx)
            99 -> return registers[0]
            else -> throw Exception("invalid opcode $opcode")
        }
    }

    throw Exception("did not find opcode 99")
}

