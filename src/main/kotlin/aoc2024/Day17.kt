package aoc2024

import java.io.File

fun main() {

    class ThreeBitMachine(
        val registers: LongArray = LongArray(3) { 0 }
    ) {
        var ip = 0
        var didJump = false
        val output = mutableListOf<Long>()

        fun combo(operand: Long) = when (operand) {
            in 0..3 -> operand
            in 4..6 -> registers[(operand-4).toInt()]
            else -> throw Exception("invalid combo operand")
        }

        fun evalop(opcode: Int, operand: Long) {
            when (opcode) {
                0 -> { // adv (divide A)
                    val numerator = registers[0]
                    val denominator = 1 shl combo(operand).toInt()
                    registers[0] = numerator / denominator
                }
                1 -> { // bxl (xor of B)
                    registers[1] = registers[1] xor operand
                }
                2 -> { // bst (B mod 8)
                    registers[1] = combo(operand) % 8
                }
                3 -> { // jnz (jump A not zero)
                    if (registers[0] != 0L) {
                        ip = operand.toInt()
                        didJump = true
                    }
                }
                4 -> { // bxc (b xor c)
                    registers[1] = registers[1] xor registers[2]
                }
                5 -> { // out
                    output.add(combo(operand) % 8)
                }
                6 -> { // bdv (divide A store in B)
                    val numerator = registers[0]
                    val denominator = 1 shl combo(operand).toInt()
                    registers[1] = numerator / denominator
                }
                7 -> { // cdv (divide A store in C)
                    val numerator = registers[0]
                    val denominator = 1 shl combo(operand).toInt()
                    registers[2] = numerator / denominator
                }
            }
        }

        fun runProgram(
            program: List<Long>,
            startingIp: Int = 0
        ) {
            ip = startingIp
            while (ip < program.size-1) {
                val opcode = program[ip].toInt()
                val operand = program[ip+1]
                evalop(opcode, operand)
                if (didJump) {
                    didJump = false
                } else {
                    ip += 2
                }
            }
        }
    }

    class Input(
        val registers: LongArray,
        val program: List<Long>
    )

    fun parseInput(): Input {
        val tokens = File("input.txt").readLines().map { it.split(": ") }
        val registers = LongArray(3) { 0 }
        registers[0] = tokens[0][1].toLong()
        registers[1] = tokens[1][1].toLong()
        registers[2] = tokens[2][1].toLong()
        val program = tokens.last().last().split(",").map { it.toLong() }
        return Input(registers, program)
    }

    val input = parseInput()

    fun findRegisterRecursive(
        prevA: Long,
        target: String,
        results: MutableList<Long>
    ) {
        for (nextOctal in 0 until 8) {
            val a = (prevA shl 3) + nextOctal
            val registers = longArrayOf(a, 0, 0)
            val machine = ThreeBitMachine(registers)
            machine.runProgram(input.program)
            val output = machine.output.joinToString(",")
            if (output == target) {
                results.add(a)
                return
            } else if (target.endsWith(output)) {
                findRegisterRecursive(a, target, results)
            }
        }
    }

    fun part1() {
        val machine = ThreeBitMachine(input.registers)
        machine.runProgram(input.program)
        println(machine.output.joinToString(","))
    }

    fun part2() {
        val results = mutableListOf<Long>()
        findRegisterRecursive(0, input.program.joinToString(","), results)
        println(results.minOf { it })
    }

    part1()
    part2()
}