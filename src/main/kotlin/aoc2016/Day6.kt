package aoc2016

import util.input
import kotlin.streams.asSequence

private fun columnCharCounts(lines: Sequence<String>) =
    lines.fold(List<Map<Char, Int>>(8) { mapOf() }) { colCounts, line ->
        colCounts.mapIndexed { col, counts ->
            counts + (line[col] to (counts[line[col]] ?: 0) + 1)
        }
    }

private fun mostFrequentCharsByColumn(lines: Sequence<String>) = columnCharCounts(lines).map { counts ->
    counts.maxBy { it.value }?.key
}.joinToString("")

private fun leastFrequentCharsByColumn(lines: Sequence<String>) = columnCharCounts(lines).map { counts ->
    counts.minBy { it.value }?.key
}.joinToString("")

fun main() {
    input("inputs/2016/6.txt").use {
        println(mostFrequentCharsByColumn(it.asSequence()))
    }

    input("inputs/2016/6.txt").use {
        println(leastFrequentCharsByColumn(it.asSequence()))
    }
}