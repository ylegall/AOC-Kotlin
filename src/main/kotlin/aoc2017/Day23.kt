package aoc2017

import util.primesBetween
import java.io.File

private object Day23 {

    private class Processor(
            val instructions: List<List<String>>,
            val debug: Boolean = false
    ) {

        val regs = HashMap<Char, Int>().apply {
            if (debug) put('a', 1)
        }
        var ip: Int = 0; private set
        var mulCount = 0

        private fun exec(op: String, x: String, y: String) {
            //println("$ip:\t$op $x $y")
            var jump = 1
            when(op) {
                "set" -> regs[x[0]] = y.symbolValue()
                "sub" -> regs[x[0]] = (regs[x[0]] ?: 0) - y.symbolValue()
                "mul" -> regs[x[0]] = (regs[x[0]] ?: 0) * y.symbolValue()
                "jnz" -> if (x.symbolValue() != 0) jump = y.symbolValue()
            }
            if (op == "mul") mulCount++
            ip += jump
        }

        private fun String?.symbolValue() = this?.let {
            if (this[0] in 'a'..'z') {
                regs[this[0]] ?: 0
            } else {
                this.toInt()
            }
        } ?: 0

        fun debug() {
            val buffer = StringBuilder("$ip: ")
            for (reg in "abcdefgh") {
                buffer.append("%9d".format(regs[reg] ?: 0))
            }
            println(buffer.toString())
        }

        fun run() {
            while (ip in instructions.indices) {
                val ins = instructions[ip]
                exec(ins[0], ins[1], ins[2])
            }
            //debug()
        }
    }

    fun findMulCount() {
        val instructions = File("inputs/2017/23.txt").readLines().map { it.split(" ") }
        val processor = Processor(instructions)
        processor.run()
        println(processor.mulCount)
    }

    fun findValueInH() {

        val primes = primesBetween(108400, 125400)
        val incrementsOf17 = (108400 .. 125400 step 17).toSet()
        println(incrementsOf17.filter { it !in primes}.size)
    }

}

fun main() {
    Day23.findMulCount()
    Day23.findValueInH()
}