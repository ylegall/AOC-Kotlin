package aoc2015

import util.input
import kotlin.streams.toList

// https://adventofcode.com/2015/day/23
object Day23 {

    private class CPU(val regs: HashMap<String, Int>) {
        var ip: Int = 0
    }

    private fun CPU.runProgram(instructions: List<List<String>>) {
        while (ip < instructions.size) {
            val ins = instructions[ip]
            //println("$ip: $ins")
            when {
                ins.size == 3 -> execOp(ins[0], ins[1], ins[2].toInt())
                ins.size == 2 -> execOp(ins[0], ins[1])
                else          -> throw IllegalArgumentException()
            }
        }
    }

    private fun CPU.execOp(code: String, r: String, jumpOffset: Int = 1) {
        var offset = 1
        when (code) {
            "hlf" -> regs[r] = regs[r]!! / 2
            "tpl" -> regs[r] = regs[r]!! * 3
            "inc" -> regs[r] = regs[r]!! + 1
            "jmp" -> offset = r.toInt()
            "jie" -> if (regs[r]!! % 2 == 0) offset = jumpOffset
            "jio" -> if (regs[r]!! == 1) offset = jumpOffset
            else  -> throw IllegalArgumentException()
        }
        ip += offset
    }

    fun run() {
        val instructions = input("inputs/2015/23.txt").use { lines ->
            lines.map {
                it.split(',', ' ')
                        .filterNot { it.isEmpty() }
                        .map { it.trim() }
            }.toList()
        }
        // part 1
        CPU(hashMapOf("a" to 0, "b" to 0)).apply {
            runProgram(instructions)
        }.also {
            println(it.regs["b"])
        }

        // part 2
        CPU(hashMapOf("a" to 1, "b" to 0)).apply {
            runProgram(instructions)
        }.also {
            println(it.regs["b"])
        }
    }

}

fun main() {
    Day23.run()
}