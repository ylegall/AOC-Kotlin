package aoc2023

import util.product
import java.io.File
import kotlin.math.max


fun main() {

    fun parseGame(line: String): List<Map<String, Int>> {
        return line
            .substring(line.indexOf(':')+2)
            .split("; ")
            .map { round ->
                round.split(", ", " ")
                    .chunked(2)
                    .associate { chunk -> chunk[1] to chunk[0].toInt() }
        }
    }

    fun part1() {
        val limits = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        val result = File("input.txt").useLines { lines ->
            lines.mapIndexed { idx, line ->
                (idx + 1) to parseGame(line)
            }.filter { (_, game) ->
                game.all { round ->
                    round.all { (color, count) -> count <= limits[color]!! }
                }
            }.sumOf { it.first }
        }
        println(result)
    }

    fun part2() {
        val keys = listOf("red", "green", "blue")
        val emptyCounts = keys.associateWith { 0 }

        val result = File("input.txt").useLines { lines ->
            lines.mapIndexed { idx, line ->
                (idx + 1) to parseGame(line)
            }.map { (_, game) ->
                val maxColors = (game + emptyCounts).reduce { maxMap, round ->
                    keys.associateWith { max(maxMap[it] ?: 0, round[it] ?: 0) }
                }
                maxColors.values.product(1)
            }.sum()
        }
        println(result)
    }

    part1()
    part2()
}