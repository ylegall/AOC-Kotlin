package aoc2025

import aoc2025.Day3.largestJoltage
import java.io.File
import java.util.PriorityQueue

private object Day3 {
    fun parseInput() = File("input.txt").readLines()

    data class IndexedDigit(
        val digit: Int,
        val index: Int
    ): Comparable<IndexedDigit> {
        override fun compareTo(other: IndexedDigit): Int {
            return if (digit == other.digit) index - other.index else other.digit - digit
        }
    }

    fun String.largestJoltage(maxDigits: Int): Long {
        var result = 0L

        val indexedDigits = this.asSequence()
            .mapIndexed { index, ch -> IndexedDigit(ch - '0', index) }
            .toList()

        val pq = PriorityQueue(indexedDigits.take(length - maxDigits + 1).toMutableList())

        var i = length - maxDigits + 1
        var lastIndex = -1
        repeat (maxDigits) {
            while (pq.peek().index <= lastIndex) {
                pq.poll()
            }
            val nextDigit = pq.poll() ?: throw Exception("ran out of digits")
            result = result * 10 + nextDigit.digit
            lastIndex = nextDigit.index
            if (i < length) {
                pq.add(indexedDigits[i++])
            }
        }
        return result
    }
}

fun main() {
    val input = Day3.parseInput()
    println(input.sumOf { it.largestJoltage(2) })
    println(input.sumOf { it.largestJoltage(12) })
}