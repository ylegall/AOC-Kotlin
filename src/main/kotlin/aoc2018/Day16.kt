package aoc2018

import java.io.File

private typealias Operation = (List<Int>, MutableList<Int>) -> Unit

private object Day16 {

    private data class Sample(
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
            { op, regs -> regs[op[3]] = op[1] },                       // seti
            { op, regs -> regs[op[3]] = if (op[1] > regs[op[2]]) 1 else 0 },        // gtir
            { op, regs -> regs[op[3]] = if (regs[op[1]] > op[2]) 1 else 0 },        // gtri
            { op, regs -> regs[op[3]] = if (regs[op[1]] > regs[op[2]]) 1 else 0 },  // gtrr
            { op, regs -> regs[op[3]] = if (op[1] == regs[op[2]]) 1 else 0 },       // eqir
            { op, regs -> regs[op[3]] = if (regs[op[1]] == op[2]) 1 else 0 },       // eqri
            { op, regs -> regs[op[3]] = if (regs[op[1]] == regs[op[2]]) 1 else 0 }  // eqrr
    )

    private fun List<Int>.applyOp(op: Operation, opcode: List<Int>): List<Int> {
        return this.toMutableList().let { op(opcode, it); it }
    }

    private fun operationMatchCount(sample: Sample): Int {
        return operations.map { op ->
            sample.before.applyOp(op, sample.opcode)
        }.count {
            it == sample.after
        }
    }

    private fun potentialOpcodes(samples: List<Sample>): Map<Int, MutableSet<Int>> {
        return operations.mapIndexed { idx, op ->
            val matches = samples.mapNotNull { sample ->
                sample.before.applyOp(op, sample.opcode).let {
                    if (it == sample.after) {
                        sample.opcode[0]
                    } else {
                        null
                    }
                }
            }.toMutableSet()
            idx to matches
        }.toMap()
    }

    private fun decodeOperations(samples: List<Sample>): Map<Int, Int> {
        val codeTable = HashMap<Int, Int>()
        val assignedCodes = HashSet<Int>()
        val potentialOpcodes = potentialOpcodes(samples)

        while (true) {
            val singleMatches = potentialOpcodes.filterKeys {
                it !in assignedCodes
            }.filterValues {
                it.size == 1
            }.mapValues {
                it.value.first()
            }

            if (singleMatches.isEmpty()) break

            for ((idx, opid) in singleMatches.entries) {
                codeTable[opid] = idx
                assignedCodes.add(idx)
            }
            potentialOpcodes.entries.forEach { it.value.removeAll(singleMatches.values) }
        }
        return codeTable
    }

    private val nonDigits = Regex("""\D+""")

    private fun String.extractNumbers(): List<Int> {
        return this.split(nonDigits).filter { it.isNotEmpty() }.map { it.toInt() }
    }

    private fun runProgram(program: List<List<Int>>, codeTable: Map<Int, Int>): List<Int> {
        val regs = mutableListOf(0, 0, 0, 0)
        for (ins in program) {
            val op = operations[codeTable[ins[0]]!!]
            op(ins, regs)
        }
        return regs
    }

    fun run() {
        val samples = File("inputs/2018/16-a.txt").useLines {
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
        println(samples.map { operationMatchCount(it) }.count { it >= 3 })

        // part 2
        val program = File("inputs/2018/16-b.txt").useLines {
            it.asSequence().map { it.extractNumbers() }.toList()
        }

        val codeTable = decodeOperations(samples)
        val result = runProgram(program, codeTable)
        println(result[0])
    }
}

fun main() {
    Day16.run()
}