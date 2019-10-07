package aoc2017

import util.input
import java.util.stream.Stream
import kotlin.math.max
import kotlin.streams.toList

private object Day8 {

    private data class Operation(
            val symbol: String,
            val op: String,
            val value: Int
    ) {
        fun eval(registers: MutableMap<String, Int>) {
            registers[symbol] = when(op) {
                "inc" -> registers.getOrDefault(symbol, 0) + value
                "dec" -> registers.getOrDefault(symbol, 0) - value
                else  -> throw Exception("invalid operation: $op")
            }
        }
    }

    private data class Condition(
            val symbol: String,
            val op: String,
            val value: Int
    ) {
        fun eval(registers: Map<String, Int>) = with(registers) {
            when(op) {
                "==" -> getOrDefault(symbol, 0) == value
                "!=" -> getOrDefault(symbol, 0) != value
                ">=" -> getOrDefault(symbol, 0) >= value
                "<=" -> getOrDefault(symbol, 0) <= value
                ">"  -> getOrDefault(symbol, 0) > value
                "<"  -> getOrDefault(symbol, 0) < value
                else -> throw Exception("invalid operation: $op")
            }
        }
    }

    private data class Instruction(val op: Operation, val condition: Condition)

    private fun parseInput(lines: Stream<String>) = lines.map { line ->
        val tokens = line.split(" ")
        Instruction(
            Operation(tokens[0], tokens[1], tokens[2].toInt()),
            Condition(tokens[4], tokens[5], tokens[6].toInt())
        )
    }.toList()

    private fun evalAndFindLargestValue(instructions: List<Instruction>): Pair<Int, Int> {
        val registers = HashMap<String, Int>()
        var maxValueAtAnyTime = 0
        for (instruction in instructions) {
            if (instruction.condition.eval(registers)) {
                instruction.op.eval(registers)
                maxValueAtAnyTime = max(maxValueAtAnyTime, registers.getOrDefault(instruction.op.symbol, 0))
            }
        }
        return registers.values.max()!! to maxValueAtAnyTime
    }

    fun run() {
        val instructions = input("inputs/2017/8.txt").use { parseInput(it) }
        println(evalAndFindLargestValue(instructions))
    }
}

fun main() {
    Day8.run()
}