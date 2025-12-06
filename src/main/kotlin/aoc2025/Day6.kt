package aoc2025

import util.split
import java.io.File

private object Day6 {

    class Input(
        val lines: List<String>,
        val operations: List<Char>
    )

    fun parseInput(): Input {
        return File("input.txt").readLines().let { lines ->
            Input(
                lines.dropLast(1),
                lines.last().split(' ').filter { it.isNotEmpty() }.map { it[0] }
            )
        }
    }

    fun Collection<Long>.eval(op: Char): Long {
        return reduce { acc, next ->
            when (op) {
                '+' -> acc + next
                '*' -> acc * next
                else -> throw Exception("invalid op: $op")
            }
        }
    }

    fun part1(input: Input) {
        val values = input.lines.map { line ->
            line.split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        }
        val result = (0 until input.operations.size).asSequence().map { col ->
            val op = input.operations[col]
            values.map { row -> row[col] }.eval(op)
        }.sum()
        println(result)
    }

    fun part2(input: Input) {
        val rows = input.lines.size
        val cols = input.lines.map { it.length }.max()!!

        val transposed = List(cols) { CharArray(rows) { ' ' } }
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                transposed[col][row] = input.lines[row].getOrNull(col) ?: ' '
            }
        }

        val values = transposed.asSequence()
            .map { String(it).trim() }
            .toList()
            .split { it.isBlank() }

        val result = values.mapIndexed { index, chunk ->
            val op = input.operations[index]
            chunk.map { it.toLong() }.eval(op)
        }.sum()
        println(result)
    }
}

fun main() {
    val input = Day6.parseInput()
    Day6.part1(input)
    Day6.part2(input)
}
