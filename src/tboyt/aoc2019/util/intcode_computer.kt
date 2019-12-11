package tboyt.aoc2019.util

fun parseProgram(line: String): List<Long> {
    return line.split(",") .map { it.toLong() }
}

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1
const val PARAM_MODE_RELATIVE = 2

data class Parameter(val value: Long, val mode: Int)

enum class IntcodeExitState {
    WAITING,
    HALTED
}

data class IntcodeComputerState(val output: List<Long>, val exitState: IntcodeExitState)

typealias IntcodeComputer = (inputs: List<Long>) -> IntcodeComputerState

fun createIntcodeComputer(program: List<Long>): IntcodeComputer {

    /* Intcode computer state */

    val addresses = program.toMutableList()
    var instructionPointer = 0L
    var relativeBase = 0L
    val extendedMemory = mutableMapOf<Long, Long>()

    /* Intcode helper methods */

    fun getAddress(address: Long): Long {
        if (address >= addresses.size) {
            return extendedMemory.getOrDefault(address, 0)
        }
        return addresses[address.toInt()]
    }

    fun getParamAddress(param: Parameter): Long {
        return when (param.mode) {
            PARAM_MODE_POSITION -> param.value
            PARAM_MODE_IMMEDIATE -> error("cannot get address of immediate param")
            PARAM_MODE_RELATIVE -> param.value + relativeBase
            else -> throw Exception("unrecognized param mode ${param.mode}")
        }
    }

    fun setAddress(addressParam: Parameter, value: Long) {
        val address = getParamAddress(addressParam)
        if (address >= addresses.size) {
            extendedMemory[address] = value
        } else {
            addresses[address.toInt()] = value
        }
    }

    fun getParamValue(param: Parameter): Long {
        return when (param.mode) {
            PARAM_MODE_POSITION -> getAddress(param.value)
            PARAM_MODE_IMMEDIATE -> param.value
            PARAM_MODE_RELATIVE -> getAddress(param.value + relativeBase)
            else -> throw Exception("unrecognized param mode ${param.mode}")
        }
    }

    /**
    Return n tokens past the current pointer position and increment the pointer position.
     */
    fun consumeParamTokens(paramCount: Int): List<Long> {
        val start = instructionPointer + 1
        val end = instructionPointer + paramCount
        val params = addresses.slice(start.toInt()..end.toInt())
        instructionPointer = end + 1
        return params
    }

    fun applyParamModes(params: List<Long>, opcode: Long): List<Parameter> {
        val paramModes = opcode.toString().dropLast(2).reversed().map { it.toString().toInt() }

        return params.mapIndexed { idx, param ->
            val paramMode = paramModes.getOrElse(idx) { PARAM_MODE_POSITION }
            Parameter(param, paramMode)
        }
    }

    fun consumeParams(paramCount: Int, opcode: Long): List<Parameter> {
        val paramTokens = consumeParamTokens(paramCount)
        return applyParamModes(paramTokens, opcode)
    }

    /* Intcode executor */

    return fun (inputs: List<Long>): IntcodeComputerState {
        var inputPointer = 0
        val outputs = ArrayList<Long>()

        while (instructionPointer < addresses.size) {
            val opcode = addresses[instructionPointer.toInt()]
            val instruction = opcode % 100
            try {
                when (instruction.toInt()) {
                    // add
                    1 -> {
                        val params = consumeParams(3, opcode)
                        val value = getParamValue(params[0]) + getParamValue(params[1])
                        setAddress(params[2], value)
                    }
                    // multiply
                    2 -> {
                        val params = consumeParams(3, opcode)
                        val value = getParamValue(params[0]) * getParamValue(params[1])
                        setAddress(params[2], value)
                    }
                    // store input in address
                    3 -> {
                        if (inputPointer == inputs.size) {
                            // wait for next input...
                            return IntcodeComputerState(outputs, IntcodeExitState.WAITING)
                        }
                        val params = consumeParams(1, opcode)
                        setAddress(params[0], inputs[inputPointer])
                        inputPointer += 1
                    }
                    // write output
                    4 -> {
                        val params = consumeParams(1, opcode)
                        val value = getParamValue(params[0])
                        outputs.add(value)
                    }
                    // jump-if-true
                    5 -> {
                        val params = consumeParams(2, opcode)
                        if (getParamValue(params[0]) != 0L) {
                            instructionPointer = getParamValue(params[1])
                        }
                    }
                    // jump-if-false
                    6 -> {
                        val params = consumeParams(2, opcode)
                        if (getParamValue(params[0]) == 0L) {
                            instructionPointer = getParamValue(params[1])
                        }
                    }
                    // less-than
                    7 -> {
                        val params = consumeParams(3, opcode)
                        if (getParamValue(params[0]) < getParamValue(params[1])) {
                            setAddress(params[2], 1)
                        } else {
                            setAddress(params[2], 0)
                        }
                    }
                    // equals
                    8 -> {
                        val params = consumeParams(3, opcode)
                        if (getParamValue(params[0]) == getParamValue(params[1])) {
                            setAddress(params[2], 1)
                        } else {
                            setAddress(params[2], 0)
                        }
                    }
                    9 -> {
                        val params = consumeParams(1, opcode)
                        relativeBase += getParamValue(params[0])
                    }
                    // halt
                    99 -> {
                        return IntcodeComputerState(outputs, IntcodeExitState.HALTED)
                    }
                    else -> throw Exception("invalid instruction $opcode")
                }
            } catch (error: Exception) {
                println("Error encountered at position $instructionPointer with opcode $opcode")
                throw error
            }
        }

        throw Exception("did not find instruction 99")
    }
}