package aoc2018

import util.input
import kotlin.streams.asSequence
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


private const val IP = 1

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
        else   -> throw Exception("bad opcode: $opcode")
    }
    regs[ops[2]] = result
}

private fun run(input: List<Pair<String, List<Int>>>, stopAtFirst: Boolean = false): Int {
    val seenRegs = mutableSetOf<Int>()
    val regs = MutableList(6) { 0 }
    //regs[0] = startValue

    while (regs[IP] in input.indices) {
        val (opcode, ops) = input[regs[IP]]
        execute(regs, opcode, ops)

        if (regs[IP] == 28) {
            if (regs[4] !in seenRegs) {
                if (stopAtFirst) return regs[4]
                seenRegs.add(regs[4])
            } else {
                break
            }
        }

        regs[IP]++
    }
    return seenRegs.last()
}

@ExperimentalTime
fun main() {
    val instructions = input("inputs/2018/21.txt").use { lines ->
        lines.asSequence().drop(1).map { line ->
            val tokens = line.split(" ")
            tokens[0] to tokens.drop(1).map { it.toInt() }
        }.toList()
    }
    println(measureTimedValue { run(instructions, stopAtFirst = true) })
    println(measureTimedValue { run(instructions) })
}