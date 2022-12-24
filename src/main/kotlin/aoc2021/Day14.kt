package aoc2021

import util.MutableCounter
import java.io.File

fun main() {

    fun interlace(parent: String, insertion: String): String {
        val newChars = CharArray(parent.length + insertion.length)
        for (i in parent.indices) {
            newChars[i*2] = parent[i]
        }
        for (i in insertion.indices) {
            newChars[i*2 + 1] = insertion[i]
        }
        return String(newChars)
    }

    fun part1(start: String, rules: Map<String, String>): Int {
        var current = start
        repeat(10) {
            val insertions = current.windowed(2).map { rules[it] }.joinToString("")
            current = interlace(current, insertions)
        }
        val counts = current.groupingBy { it }.eachCount()
        val mostCommon = counts.maxOf { it.value }
        val leastCommon = counts.minOf { it.value }
        return mostCommon - leastCommon
    }

    fun part2(start: String, rules: Map<String, String>): Long {
        val pairCounts = MutableCounter<String>()
        val letterCounts = MutableCounter<Char>()
        start.forEach { letterCounts.increment(it) }
        start.windowed(2).forEach { pairCounts.increment(it) }

        repeat(40) {
            val newPairCounts = mutableMapOf<String, Long>()
            for ((pair, count) in pairCounts.copy()) {
                val next = rules[pair]!!
                letterCounts.increment(next[0], count)
                val leftPair = pair.substring(0, 1) + next
                val rightPair = next + pair.substring(1, 2)
                newPairCounts[leftPair] = count
                newPairCounts[rightPair] = count
                pairCounts.decrement(pair, count)
                pairCounts.increment(leftPair, count)
                pairCounts.increment(rightPair, count)
            }

        }
        val mostCommon = letterCounts.maxOf { it.value }
        val leastCommon = letterCounts.minOf { it.value }
        return mostCommon - leastCommon
    }

    val lines = File("inputs/2021/input.txt").readLines()
    val start = lines.first()
    val rules = lines.drop(2).map { line ->
        line.split(" -> ").let {
            it[0] to it[1]
        }
    }.toMap()

    println(part1(start, rules))
    println(part2(start, rules))
}