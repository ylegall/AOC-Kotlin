package aoc2016

import util.input
import kotlin.streams.toList


object Day12 {

    private class State(
            private var pc: Int = 0,
            private val regs: MutableList<Int> = mutableListOf(0, 0, 0, 0)
    ) {
        fun exec(program: List<List<String>>): List<Int> {
            while (pc < program.size) {
                val instruction = program[pc]
                exec(instruction)
                pc += 1
            }
            return regs
        }

        fun exec(op: List<String>) {
            when (op[0]) {
                "cpy" -> cpy(op[1], op[2][0])
                "inc" -> inc(op[1][0])
                "dec" -> dec(op[1][0])
                "jnz" -> jnz(op[1], op[2])
            }
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
            val op1Value = if (op1.isRegister()) {
                getRegister(op1)
            } else {
                op1.toInt()
            }

            if (op1Value != 0) {
                pc += op2.toInt()
                pc -= 1
            }
        }

        private fun getRegister(str: String): Int = regs[str[0] - 'a']
    }

    fun String.isRegister() = this[0] in 'a' .. 'd'

    fun run() {
        val state = State()
        input("inputs/2016/12.txt").use {
            val program = it.toList().map { it.split(" ") }
            println(state.exec(program))
        }
    }
}

fun main() {
    Day12.run()
}