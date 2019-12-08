package aoc2019

import java.io.File


class IntCodeProcessor(
        private val codes: MutableList<Int>,
        private val inputSupplier: () -> Int = { 0 },
        private val outputConsumer: (Int) -> Unit = { println(it) }
) {

    private var ip = 0
    var halted = false; private set

    class Instruction(
            val opcode: Int,
            val op1: Int,
            val op2: Int = 0,
            val op3: Int = 0
    )

    private fun parseInstruction(): Instruction {
        val modes = codes[ip] / 100
        val mode1 = modes % 10
        val mode2 = (modes / 10) % 10
        val mode3 = (modes / 100) % 10
        val op1 = getOpValue(ip + 1, mode1)
        val op2 = getOpValue(ip + 2, mode2)
        val op3 = getOpValue(ip + 3, mode3)
        return Instruction(codes[ip] % 100, op1, op2, op3)
    }

    private fun getOpValue(addr: Int, mode: Int) = if (mode == 0) codes.getOrElse(addr) { 0 } else addr

    private fun exec() {
        val ins = parseInstruction()
        when (ins.opcode) {
            1 -> add(ins.op1, ins.op2, ins.op3)
            2 -> mul(ins.op1, ins.op2, ins.op3)
            3 -> input(ins.op1)
            4 -> output(ins.op1)
            5 -> jumpIfTrue(ins.op1, ins.op2)
            6 -> jumpIfFalse(ins.op1, ins.op2)
            7 -> lessThan(ins.op1, ins.op2, ins.op3)
            8 -> equals(ins.op1, ins.op2, ins.op3)
            99 -> halted = true
            else -> throw Exception("bad opcode: ${codes[ip]}")
        }
    }

    private fun lessThan(op1: Int, op2: Int, dst: Int) {
        codes[dst] = if (codes[op1] < codes[op2]) 1 else 0
        ip += 4
    }

    private fun equals(op1: Int, op2: Int, dst: Int) {
        codes[dst] = if (codes[op1] == codes[op2]) 1 else 0
        ip += 4
    }

    private fun jumpIfTrue(op1: Int, op2: Int) {
        if (codes[op1] != 0) {
            ip = codes[op2]
        } else {
            ip += 3
        }
    }

    private fun jumpIfFalse(op1: Int, op2: Int) {
        if (codes[op1] == 0) {
            ip = codes[op2]
        } else {
            ip += 3
        }
    }

    private fun input(op1: Int) {
        codes[op1] = inputSupplier()
        ip += 2
    }

    private fun output(op1: Int) {
        outputConsumer(codes[op1])
        ip += 2
    }

    private fun add(op1: Int, op2: Int, dst: Int) {
        codes[dst] = codes[op1] + codes[op2]
        ip += 4
    }

    private fun mul(op1: Int, op2: Int, dst: Int) {
        codes[dst] = codes[op1] * codes[op2]
        ip += 4
    }

    fun run() {
        while (!halted) {
            exec()
        }
    }
}

fun loadIntCodeInstructions(filePath: String) = File(filePath).readText().trim().split(",").map {
    it.toInt()
}.toMutableList()