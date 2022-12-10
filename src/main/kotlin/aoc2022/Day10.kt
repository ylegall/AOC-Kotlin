package aoc2022

import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("input.txt").useLines { lines ->
        lines.flatMap { line ->
            if (line == "noop") {
                listOf(0)
            } else {
                val arg = line.removePrefix("addx ").toInt()
                listOf(0, arg)
            }
        }.toList()
    }

    fun part1() {
        val snapshots = List(6) { 20 + it * 40 }.toSet()

        val totalSignalStrength = input.asSequence()
                .scan(1) { sum, arg -> sum + arg }
                .mapIndexed { idx, sum -> (idx + 1) * sum }
                .filterIndexed { idx, _ -> (idx + 1) in snapshots }
                .sum()
        println(totalSignalStrength)
    }

    fun part2() {
        val lines = input.asSequence()
                .scan(1) { sum, arg -> sum + arg }
                .chunked(40)
                .map { chunk ->
                    chunk.mapIndexed { idx, spritePos ->
                        if (abs(idx - spritePos) <= 1) '#' else '.'
                    }.joinToString("")
                }
                .joinToString("\n")
        println(lines)
    }

    part1()
    part2()
}
