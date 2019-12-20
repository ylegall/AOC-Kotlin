package aoc2019

import aoc2019.ProcessorState.*
import java.io.File

enum class ProcessorState { RUNNING, PAUSED, HALTED }

class IntCodeProcessor(
        private val memory: MutableMap<Int, Long>,
        private val inputSupplier: () -> Long = { 0L },
        private val outputConsumer: (Long) -> Unit = { println(it) }
) {

    constructor(
            codes: Iterable<Long>,
            inputSupplier: () -> Long = { 0L },
            outputConsumer: (Long) -> Unit = { println(it) }
    ): this(
            codes.mapIndexed { index, value -> index to value }.toMap().toMutableMap(),
            inputSupplier,
            outputConsumer
    )

    private var ip = 0
    private var relativeBase = 0L
    var state = RUNNING; private set
    var pauseOnOutput = false

    private class Instruction(
            val opcode: Long,
            val op1: Long,
            val op2: Long = 0,
            val op3: Long = 0
    )

    private fun parseInstruction(): Instruction {
        val opcode = read(ip)
        val modes = opcode / 100L
        val mode1 = modes % 10L
        val mode2 = (modes / 10) % 10L
        val mode3 = (modes / 100) % 10L
        val op1 = getOpValue(ip + 1, mode1)
        val op2 = getOpValue(ip + 2, mode2)
        val op3 = getOpValue(ip + 3, mode3)
        return Instruction(opcode % 100, op1, op2, op3)
    }

    private fun getOpValue(addr: Int, mode: Long) = when (mode) {
        0L -> read(addr)
        1L -> addr.toLong()
        2L -> read(addr) + relativeBase
        else -> throw Exception("unsupported mode: $mode")
    }

    fun setMemoryValues(vararg values: Pair<Int, Long>) {
        values.forEach { memory[it.first] = it.second }
    }

    fun read(idx: Int): Long {
        check(idx >= 0) { "invalid memory address: $idx" }
        return memory[idx] ?: 0L
    }

    private fun read(idx: Long) = read(idx.toInt())

    private fun write(idx: Long, value: Long){
        check(idx >= 0) { "invalid memory address: $idx" }
        memory[idx.toInt()] = value
    }

    private fun exec() {
        val ins = parseInstruction()
        when (ins.opcode) {
            1L -> add(ins.op1, ins.op2, ins.op3)
            2L -> mul(ins.op1, ins.op2, ins.op3)
            3L -> input(ins.op1)
            4L -> output(ins.op1)
            5L -> jumpIfTrue(ins.op1, ins.op2)
            6L -> jumpIfFalse(ins.op1, ins.op2)
            7L -> lessThan(ins.op1, ins.op2, ins.op3)
            8L -> equals(ins.op1, ins.op2, ins.op3)
            9L -> setBase(ins.op1)
            99L -> state = HALTED
            else -> throw Exception("bad opcode: ${ins.opcode}")
        }
    }

    private fun setBase(op1: Long) {
        relativeBase += read(op1)
        ip += 2
    }

    private fun lessThan(op1: Long, op2: Long, dst: Long) {
        write(dst, if (read(op1) < read(op2)) 1 else 0)
        ip += 4
    }

    private fun equals(op1: Long, op2: Long, dst: Long) {
        write(dst, if (read(op1) == read(op2)) 1 else 0)
        ip += 4
    }

    private fun jumpIfTrue(op1: Long, op2: Long) {
        if (read(op1) != 0L) {
            ip = read(op2).toInt()
        } else {
            ip += 3
        }
    }

    private fun jumpIfFalse(op1: Long, op2: Long) {
        if (read(op1) == 0L) {
            ip = read(op2).toInt()
        } else {
            ip += 3
        }
    }

    private fun input(op1: Long) {
        write(op1, inputSupplier())
        ip += 2
    }

    private fun output(op1: Long) {
        //println("output $op1")
        outputConsumer(read(op1))
        ip += 2
        if (pauseOnOutput) state = PAUSED
    }

    private fun add(op1: Long, op2: Long, dst: Long) {
        write(dst, read(op1) + read(op2))
        ip += 4
    }

    private fun mul(op1: Long, op2: Long, dst: Long) {
        write(dst, read(op1) * read(op2))
        ip += 4
    }

    fun copy(
            memory: MutableMap<Int, Long> = this.memory.toMutableMap(),
            inputSupplier: () -> Long = this.inputSupplier,
            outputConsumer: (Long) -> Unit = this.outputConsumer
    ): IntCodeProcessor {
        val processor = IntCodeProcessor(memory, inputSupplier, outputConsumer)
        processor.ip = ip
        processor.pauseOnOutput = pauseOnOutput
        processor.relativeBase = relativeBase
        return processor
    }

    fun pause() { state = PAUSED }

    fun run() {
        if (state == HALTED) return
        state = RUNNING
        while (state == RUNNING) {
            exec()
        }
    }
}

fun loadIntCodeInstructions(filePath: String) = File(filePath).readText().trim().split(",").map {
    it.toLong()
}