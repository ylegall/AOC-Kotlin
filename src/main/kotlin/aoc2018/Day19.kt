package aoc2018

import util.input
import kotlin.streams.asSequence

private const val IP = 3

private fun execute(regs: MutableList<Int>, opcode: String, ops: List<Int>) {
    val result = when (opcode) {
        "addr" -> regs[ops[0]] + regs[ops[1]]
        "addi" -> regs[ops[0]] + ops[1]
        "mulr" -> regs[ops[0]] * regs[ops[1]]
        "muli" -> regs[ops[0]] * ops[1]
        "banr" -> regs[ops[0]] and regs[ops[1]]
        "bani" -> regs[ops[0]] and ops[1]
        "borr" -> regs[ops[0]] or regs[ops[1]]
        "bori" -> regs[ops[0]] or ops[1]
        "setr" -> regs[ops[0]]
        "seti" -> ops[0]
        "gtir" -> if (ops[0] > regs[ops[1]]) 1 else 0
        "gtri" -> if (regs[ops[0]] > ops[1]) 1 else 0
        "gtrr" -> if (regs[ops[0]] > regs[ops[1]]) 1 else 0
        "eqir" -> if (ops[0] == regs[ops[1]]) 1 else 0
        "eqri" -> if (regs[ops[0]] == ops[1]) 1 else 0
        "eqrr" -> if (regs[ops[0]] == regs[ops[1]]) 1 else 0
        else -> throw Exception("bad opcode: $opcode")
    }
    regs[ops[2]] = result
}

private fun run(input: List<Pair<String, List<Int>>>) {
    val regs = MutableList(6) { 0 }
    while (regs[IP] in input.indices) {
        val (opcode, ops) = input[regs[IP]]
        execute(regs, opcode, ops)
        regs[IP]++
    }
    println(regs)
}

private fun Int.factors(): List<Int> {
    val result = mutableListOf<Int>()
    (1 until Math.sqrt(this.toDouble()).toInt() + 1).forEach { factor ->
        if (this % factor == 0) {
            result.add(factor)
            result.add(this / factor)
        }
    }
    return result
}


fun main() {
    val instructions = input("inputs/2018/19.txt").use { lines ->
        lines.asSequence().drop(1).map { line ->
            val tokens = line.split(" ")
            tokens[0] to tokens.drop(1).map { it.toInt() }
        }.toList()
    }
    run(instructions)
    println(10551424.factors().sorted())
    println(10551424.factors().sum())
}