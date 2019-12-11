package tboyt.aoc2019.util

fun parseProgram(line: String): List<Int> {
    return line.split(",") .map { it.toInt() }
}

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1

data class Parameter(val value: Int, val mode: Int)

enum class IntcodeExitState {
    WAITING,
    HALTED
}

data class IntcodeComputerState(val output: List<Int>, val exitState: IntcodeExitState)

typealias IntcodeComputer = (inputs: List<Int>) -> IntcodeComputerState

fun createIntcodeComputer(program: List<Int>): IntcodeComputer {
    val addresses = program.toMutableList()
    var instructionPointer = 0

    return fun (inputs: List<Int>): IntcodeComputerState {
        var inputPointer = 0
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
                        val value = getParam(params[0])
                        outputs.add(value)
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