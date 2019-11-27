package aoc2016

import util.input
import kotlin.streams.toList

private object Day23 {

    private class State(
            private var pc: Int = 0,
            private val regs: MutableList<Int> = mutableListOf(0, 0, 0, 0),
            private val modifiedInstructions: MutableMap<Int, List<String>> = mutableMapOf()
    ) {
        fun exec(program: List<List<String>>, eggs: Int = 7): List<Int> {
            regs[0] = eggs
            while (pc < program.size) {
                val instruction = modifiedInstructions[pc] ?: program[pc]
                exec(instruction, program)
                pc += 1
            }
            return regs
        }

        fun exec(op: List<String>, program: List<List<String>>) {
            when (op[0]) {
                "cpy" -> cpy(op[1], op[2][0])
                "inc" -> inc(op[1][0])
                "dec" -> dec(op[1][0])
                "jnz" -> jnz(op[1], op[2])
                "tgl" -> tgl(op[1], program)
            }
            println(regs)
        }

        private fun tgl(op1: String, program: List<List<String>>) {
            val newPc = pc + getRegister(op1)
            if (newPc !in program.indices) return
            val modifiedInst = program[newPc]
            val newInstruction = when (modifiedInst[0]) {
                "cpy" -> listOf("jnz")
                "inc" -> listOf("dec")
                "dec" -> listOf("inc")
                "jnz" -> listOf("cpy")
                "tgl" -> listOf("inc")
                else -> throw IllegalStateException()
            } + modifiedInst.subList(1, modifiedInst.size)
            modifiedInstructions[newPc] = newInstruction
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

    fun run() {
        val program = input("inputs/2016/23.txt").use {
            it.toList().map { it.split(" ") }
        }
        val state = State()
        println(state.exec(program))
    }

}

fun main() {
    Day23.run()
}