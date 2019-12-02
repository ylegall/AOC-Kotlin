package aoc2019

import java.io.File

private object Day2 {

    private const val TARGET = 19690720

    private class IntProcessor(private val codes: MutableList<Int>) {

        private var ip = 0
        var halted = false; private set

        fun exec() {
            val op1 = codes[ip + 1]
            val op2 = codes[ip + 2]
            val dst = codes[ip + 3]
            when (codes[ip]) {
                1  -> add(op1, op2, dst)
                2  -> mul(op1, op2, dst)
                99 -> halted = true
                else -> throw Exception("bad opcode: ${codes[ip]}")
            }
            ip += 4
        }

        private fun add(op1: Int, op2: Int, dst: Int) {
            codes[dst] = codes[op1] + codes[op2]
        }

        private fun mul(op1: Int, op2: Int, dst: Int) {
            codes[dst] = codes[op1] * codes[op2]
        }
    }

    private fun parseInput() = File("inputs/2019/2.txt").readText().split(",").map {
        it.trim().toInt()
    }

    private fun processWithInitialSettings(codes: MutableList<Int>, a: Int = 0, b: Int = 0): Int {
        codes[1] = a
        codes[2] = b
        val processor = IntProcessor(codes)
        while (!processor.halted) {
            processor.exec()
        }
        return codes[0]
    }

    fun findValueAtPositionZero(): Int {
        val codes = parseInput().toMutableList()
        return processWithInitialSettings(codes, 12, 2)
    }

    fun findInputsForTargetValue(): Int {
        val codes = parseInput()
        for (i in 0 .. 99) {
            for (j in 0 .. 99) {
                val result = processWithInitialSettings(codes.toMutableList(), i, j)
                if (result == TARGET) {
                    return 100 * i + j
                }
            }
        }
        throw Exception("target not found")
    }
}

fun main() {
    println(Day2.findValueAtPositionZero())
    println(Day2.findInputsForTargetValue())
}