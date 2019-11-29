package aoc2016

import java.io.File

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
    File("inputs/2016/6.txt").useLines {
        println(mostFrequentCharsByColumn(it.asSequence()))
    }

    File("inputs/2016/6.txt").useLines {
        println(leastFrequentCharsByColumn(it.asSequence()))
    }
}