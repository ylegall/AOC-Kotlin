package aoc2025

import java.io.File

private object Day2 {

    data class IdRange(
        val low: String,
        val high: String
    )

    fun parseInput(): List<IdRange> {
        return File("input.txt").readText()
            .split(',', '-')
            .chunked(2)
            .map { IdRange(it[0], it[1]) }
    }

    fun String.isValidPart1(): Boolean {
        if (length % 2 == 1) return true
        val prefix = take(length/2)
        val suffix = drop(length/2)
        return prefix != suffix
    }

    fun String.hasRepeatingPattern(): Boolean {
        for (prefixLength in 1 .. this.length/2) {
            val prefix = this.take(prefixLength)
            val suffix = this.drop(prefixLength).chunked(prefixLength)
            val hasPattern = suffix.all { it == prefix }
            if (hasPattern) return true
        }
        return false
    }

    fun part1(idRanges: List<IdRange>) {
        val result = idRanges.sumOf { idRange ->
            (idRange.low.toLong() .. idRange.high.toLong())
                .filter { !it.toString().isValidPart1() }
                .sum()
        }
        println(result)
    }

    fun part2(idRanges: List<IdRange>) {
        val result = idRanges.sumOf { idRange ->
            (idRange.low.toLong() .. idRange.high.toLong())
                .filter { it.toString().hasRepeatingPattern() }
                .sum()
        }
        println(result)
    }
}

fun main() {
    val ranges = Day2.parseInput()
    Day2.part1(ranges)
    Day2.part2(ranges)
}