package tboyt.aoc2019.day05B

import tboyt.aoc2019.util.expectResult
import tboyt.aoc2019.util.readDataFile

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1

const val testProgram = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"

fun main() {
    val input = readDataFile("day05")
//    expectResult(runProgram(lineToAddresses("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), 0), 0)
//    expectResult(runProgram(lineToAddresses("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), 1), 1)
//    expectResult(runProgram(lineToAddresses("3,3,1105,-1,9,1101,0,0,12,4,12,99,1"), 0), 0)
//    expectResult(runProgram(lineToAddresses("3,3,1105,-1,9,1101,0,0,12,4,12,99,1"), 1), 1)
//    expectResult(runProgram(lineToAddresses(testProgram), 7), 999)
//    expectResult(runProgram(lineToAddresses(testProgram), 8), 1000)
//    expectResult(runProgram(lineToAddresses(testProgram), 9), 1001)
    println(runProgram(lineToAddresses(input[0]), 5))
}

fun lineToAddresses(line: String): List<Int> {
    return line
            .split(",") .map { it.toInt() }
}

data class Parameter(val value: Int, val mode: Int)

fun runProgram(input: List<Int>, inputValue: Int): Int {
    val addresses = input.toMutableList()
    var instructionPointer = 0
    val outputs = ArrayList<Int>()

    fun getAddress(address: Int): Int {
        return addresses[address]
    }

    fun setAddress(address: Parameter, value: Int) {
        addresses[address.value] = value
    }

    fun getParam(param: Parameter): Int {
        return when (param.mode) {
            PARAM_MODE_POSITION -> getAddress(param.value)
            PARAM_MODE_IMMEDIATE -> param.value
            else -> throw Exception("unrecognized param mode ${param.mode}")
        }
    }

    /**
    Return n tokens past the current pointer position and increment the pointer position.
     */
    fun consumeParamTokens(paramCount: Int): List<Int> {
        val start = instructionPointer + 1
        val end = instructionPointer + paramCount
        val params = addresses.slice(start..end)
        instructionPointer = end + 1
        return params
    }

    fun applyParamModes(params: List<Int>, opcode: Int): List<Parameter> {
        val paramModes = opcode.toString().dropLast(2).reversed().map { it.toString().toInt() }

        return params.mapIndexed { idx, param ->
            val paramMode = paramModes.getOrElse(idx) { PARAM_MODE_POSITION }
            Parameter(param, paramMode)
        }
    }

    fun consumeParams(paramCount: Int, opcode: Int): List<Parameter> {
        val paramTokens = consumeParamTokens(paramCount)
        return applyParamModes(paramTokens, opcode)
    }

    while (instructionPointer < addresses.size) {
        val opcode = addresses[instructionPointer]
        val instruction = opcode % 100
        try {
            when (instruction) {
                // add
                1 -> {
                    val params = consumeParams(3, opcode)
                    val value = getParam(params[0]) + getParam(params[1])
                    setAddress(params[2], value)
                }
                // multiply
                2 -> {
                    val params = consumeParams(3, opcode)
                    val value = getParam(params[0]) * getParam(params[1])
                    setAddress(params[2], value)
                }
                // store input in address
                3 -> {
                    val params = consumeParams(1, opcode)
                    setAddress(params[0], inputValue)
                }
                // store and test output
                4 -> {
                    val params = consumeParams(1, opcode)
                    val value = getParam(params[0])
                    outputs.add(value)
                    if (value != 0) {
                        println("non-zero output $value at address ${params[0].value}")
                    }
                }
                // jump-if-true
                5 -> {
                    val params = consumeParams(2, opcode)
                    if (getParam(params[0]) != 0) {
                        instructionPointer = getParam(params[1])
                    }
                }
                // jump-if-false
                6 -> {
                    val params = consumeParams(2, opcode)
                    if (getParam(params[0]) == 0) {
                        instructionPointer = getParam(params[1])
                    }
                }
                // less-than
                7 -> {
                    val params = consumeParams(3, opcode)
                    if (getParam(params[0]) < getParam(params[1])) {
                        setAddress(params[2], 1)
                    } else {
                        setAddress(params[2], 0)
                    }
                }
                // equals
                8 -> {
                    val params = consumeParams(3, opcode)
                    if (getParam(params[0]) == getParam(params[1])) {
                        setAddress(params[2], 1)
                    } else {
                        setAddress(params[2], 0)
                    }
                }
                // halt
                99 -> {
                    return outputs.last()
                }
                else -> throw Exception("invalid instruction $opcode")
            }
        } catch (error: Exception) {
            println("Error encounted at position $instructionPointer with opcode $opcode")
            throw error
        }
    }

    throw Exception("did not find instruction 99")
}

