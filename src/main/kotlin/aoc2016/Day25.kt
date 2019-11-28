package aoc2016

import java.io.File

private object Day25 {

    private class Processor(
            startValue: Int = 0,
            private var pc: Int = 0,
            private val regs: MutableList<Int> = mutableListOf(0, 0, 0, 0)
    ) {

        val transmittedValues = ArrayList<Int>()

        init { regs[0] = startValue }

        fun exec(program: List<List<String>>): List<Int> {
            while (pc < program.size) {
                exec(program[pc])
                pc += 1
            }
            return regs
        }

        private fun exec(op: List<String>) {
            when (op[0]) {
                "cpy" -> cpy(op[1], op[2][0])
                "inc" -> inc(op[1][0])
                "dec" -> dec(op[1][0])
                "jnz" -> jnz(op[1], op[2])
                "out" -> out(op[1])
            }
            //println("$pc: $regs")
        }

        private fun out(op1: String) {
            transmittedValues.add(getOpValue(op1).also { println("out $it") })
            if (transmittedValues.size > 12) pc = 100000
        }

        private fun cpy(op1: String, op2: Char) {
            regs[op2 - 'a'] = if (op1.isRegister()) {
                getRegister(op1)
            } else {
                op1.toInt()
            }
        }

        private fun inc(op1: Char) {
            regs[op1 - 'a'] += 1
        }

        private fun dec(op1: Char) {
            regs[op1 - 'a'] -= 1
        }

        private fun jnz(op1: String, op2: String) {
            val op1Value = getOpValue(op1)
            if (op1Value != 0) {
                pc += getOpValue(op2)
                pc -= 1
            }
        }

        private fun getRegister(str: String): Int = regs[str[0] - 'a']

        private fun getOpValue(op: String): Int {
            return if (op.isRegister()) {
                getRegister(op)
            } else {
                op.toInt()
            }
        }
    }

    private fun String.isRegister() = this[0] in 'a'..'d'

    private fun parseInput() = File("inputs/2016/25.txt").readLines().map { it.split(" ") }

    fun findLowestValueForRegisterA() {
        val program = parseInput()
        val processor = Processor(158)
        processor.exec(program)
    }
}

fun main() {
    Day25.findLowestValueForRegisterA()
}