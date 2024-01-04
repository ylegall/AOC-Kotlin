package aoc2015

import java.io.File

fun main() {

    fun totalWrappingPaper(lines: List<List<Int>>): Int {
        return lines.sumOf {
            val dimensions = listOf(it[0] * it[1], it[1] * it[2], it[0] * it[2])
            dimensions.min() + 2 * dimensions.sum()
        }
    }

    fun totalRibbon(lines: List<List<Int>>): Int {
        return lines.sumOf { dims ->
            val minPerimeter = dims.sorted().take(2).sum() * 2
            val volume = dims.reduce { prod, next -> prod * next }
            minPerimeter + volume
        }
    }

    val lines = File("input.txt").useLines { lines ->
        lines.map { line ->
            line.split("x").map { it.toInt() }
        }.toList()
    }
    println(totalWrappingPaper(lines))
    println(totalRibbon(lines))
}