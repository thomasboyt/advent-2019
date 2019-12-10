package tboyt.aoc2019.day05A
import tboyt.aoc2019.util.readDataFile

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1

fun main() {
    val input = readDataFile("day05")
    println(runProgram(lineToAddresses(input[0]), 1))
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

    fun addOp(a: Parameter, b: Parameter, resultAddress: Parameter) {
        setAddress(resultAddress, getParam(a) + getParam(b))
    }

    fun multOp(a: Parameter, b: Parameter, resultAddress: Parameter) {
        setAddress(resultAddress, getParam(a) * getParam(b))
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
                1 -> {
                    val params = consumeParams(3, opcode)
                    addOp(params[0], params[1], params[2])
                }
                2 -> {
                    val params = consumeParams(3, opcode)
                    multOp(params[0], params[1], params[2])
                }
                3 -> {
                    val params = consumeParams(1, opcode)
                    setAddress(params[0], inputValue)
                }
                4 -> {
                    val params = consumeParams(1, opcode)
                    outputs.add(getParam(params[0]))
                    if (getParam(params[0]) == 0) {
                        println("successful output")
                    } else {
                        println("bad output")
                    }
                }
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

