package aoc2021

import util.sorted
import java.io.File

fun main() {

    class Entry(
        val patterns: List<String>,
        val output: List<String>
    )

    class Codec {
        val patterns = mutableMapOf<String, Int>()
        val digits = mutableMapOf<Int, String>()

        fun addMapping(digit: Int, pattern: String) {
            val sortedPattern = pattern.sorted()
            digits[digit] = sortedPattern
            patterns[sortedPattern] = digit
        }
    }

    val uniqueSegmentCounts = mapOf(
        2 to 1,
        3 to 7,
        4 to 4,
        7 to 8
    )

    fun identifyUnique(entry: Entry, codec: Codec) {
        for (pattern in entry.patterns) {
            if (pattern.length in uniqueSegmentCounts) {
                val digit = uniqueSegmentCounts[pattern.length]!!
                codec.addMapping(digit, pattern)
            }
        }
    }

    fun commonSegments(pattern1: String, pattern2: String): Int {
        return pattern1.toSet().intersect(pattern2.toSet()).size
    }

    fun decode(pattern: String, codec: Codec): Int {
        return when (pattern.length) {
            5 -> {
                when (commonSegments(pattern, codec.digits[4]!!)) {
                    2 -> 2
                    else -> {
                        if (commonSegments(pattern, codec.digits[7]!!) == 3) {
                            3
                        } else {
                            5
                        }
                    }
                }
            }
            6 -> {
                when (commonSegments(pattern, codec.digits[4]!!)) {
                    4 -> 9
                    else -> {
                        if (commonSegments(pattern, codec.digits[1]!!) == 2) {
                            0
                        } else {
                            6
                        }
                    }
                }
            }
            else -> throw Exception("bad segment count")
        }
    }

    fun part1(input: List<Entry>): Long {
        return input.sumOf { entry ->
            entry.output.sumOf { if (it.length in uniqueSegmentCounts) 1L else 0L }
        }
    }

    fun decodeEntry(entry: Entry, codec: Codec) {
        identifyUnique(entry, codec)
        for (pattern in entry.patterns) {
            val sortedPattern = pattern.sorted()
            if (sortedPattern !in codec.patterns) {
                val digit = decode(sortedPattern, codec)
                codec.addMapping(digit, sortedPattern)
            }
        }
        println(codec.digits)
    }

    fun part2(input: List<Entry>): Long {
        var total = 0L
        for (entry in input) {
            val codec = Codec()
            decodeEntry(entry, codec)
            val output = entry.output.joinToString("") { segments -> codec.patterns[segments.sorted()].toString() }.toLong()
            total += output
        }
        return total
    }

    val input = File("inputs/2021/8.txt").useLines { lines ->
        lines.map { line ->
            val parts = line.split(" | ")
            Entry(parts[0].split(" "), parts[1].split(" "))
        }.toList()
    }

    println(part1(input))
    println(part2(input))
}