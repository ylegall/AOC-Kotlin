package aoc2018

import util.input
import kotlin.streams.asSequence

private typealias Operation = (List<Int>, MutableList<Int>) -> Unit

data class Sample(
        val before: List<Int>,
        val opcode: List<Int>,
        val after: List<Int>
)

private val operations = listOf<Operation>(
        { op, regs -> regs[op[3]] = regs[op[1]] + regs[op[2]] },   // addr
        { op, regs -> regs[op[3]] = regs[op[1]] + op[2] },         // addi
        { op, regs -> regs[op[3]] = regs[op[1]] * regs[op[2]] },   // mulr
        { op, regs -> regs[op[3]] = regs[op[1]] * op[2] },         // muli
        { op, regs -> regs[op[3]] = regs[op[1]] and regs[op[2]] }, // banr
        { op, regs -> regs[op[3]] = regs[op[1]] and op[2] },       // bani
        { op, regs -> regs[op[3]] = regs[op[1]] or regs[op[2]] },  // borr
        { op, regs -> regs[op[3]] = regs[op[1]] or op[2] },        // bori
        { op, regs -> regs[op[3]] = regs[op[1]] },                 // setr
        { op, regs -> regs[op[3]] = op[1] },                       // setr
        { op, regs -> regs[op[3]] = if (op[1] > regs[op[2]]) 1 else 0 },        // gtir
        { op, regs -> regs[op[3]] = if (regs[op[1]] > op[2]) 1 else 0 },        // gtri
        { op, regs -> regs[op[3]] = if (regs[op[1]] > regs[op[2]]) 1 else 0 },  // gtrr
        { op, regs -> regs[op[3]] = if (op[1] == regs[op[2]]) 1 else 0 },       // eqir
        { op, regs -> regs[op[3]] = if (regs[op[1]] == op[2]) 1 else 0 },       // eqri
        { op, regs -> regs[op[3]] = if (regs[op[1]] == regs[op[2]]) 1 else 0 }  // eqrr
)

private fun matchingOperations(sample: Sample): Int {
    return operations.map { op ->
        sample.before.toMutableList().let { op(sample.opcode, it); it }
    }.count {
        it == sample.after
    }
}

private val nonDigits = Regex("""\D+""")

private fun String.extractNumbers(): List<Int> {
    return this.split(nonDigits).filter { it.isNotEmpty() }.map { it.toInt() }
}

fun main() {
    val samples = input("inputs/2018/16-a.txt").use {
        it.asSequence().filter {
            it.isNotEmpty()
        }.chunked(3).map {
            Sample(
                    it[0].extractNumbers(),
                    it[1].extractNumbers(),
                    it[2].extractNumbers()
            )
        }.toList()
    }

    // part 1
    println(samples.map { matchingOperations(it) }.count { it >= 3 })
}