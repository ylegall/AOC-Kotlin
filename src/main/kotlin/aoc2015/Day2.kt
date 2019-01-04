package aoc2015

import util.input
import kotlin.streams.toList

private fun totalWrappingPaper(lines: List<List<Int>>): Int {
    return lines.map {
        val dimensions = listOf(it[0] * it[1], it[1] * it[2], it[0] * it[2])
        dimensions.min()!! + 2 * dimensions.sum()
    }.sum()
}

private fun totalRibbon(lines: List<List<Int>>): Int {
    return lines.map { dims ->
        val minPerimeter = dims.sorted().take(2).sum() * 2
        val volume = dims.reduce { prod, next -> prod * next }
        minPerimeter + volume
    }.sum()
}

fun main() {
    val lines = input("inputs/2015/2.txt").use {
        it.map { it.split("x").map { it.toInt() } }.toList()
    }
    println(totalWrappingPaper(lines))
    println(totalRibbon(lines))
}