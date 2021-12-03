package aoc2021

import java.io.File

fun main() {

    fun mostCommonBits(input: List<String>): List<Int> {
        val counts = IntArray(input[0].length)
        for (bitString in input) {
            for (i in bitString.indices) {
                if (bitString[i] == '1') {
                    counts[i] += 1
                }
            }
        }
        return counts.map { if (it >= input.size / 2.0) 1 else 0 }
    }

    fun filterList(input: List<String>, operation: (String, Int, List<Int>) -> Boolean): Int {
        var remaining = input.toList()
        var idx = 0
        while (remaining.size > 1 ) {
            val counts = mostCommonBits(remaining)
            remaining = remaining.filter { operation(it, idx, counts) }
            idx++
        }
        return remaining.first().toInt(2)
    }

    fun part1(input: List<String>): Int {
        val counts = mostCommonBits(input)
        val gamma = counts.joinToString("").toInt(2)
        val epsilon = (counts.joinToString("") { if (it == 0) "1" else "0" }).toInt(2)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxygenRating = filterList(input) { bits, i, counts -> bits[i].digitToInt() == counts[i] }
        val scrubberRating = filterList(input) { bits, i, counts -> bits[i].digitToInt() != counts[i] }
        return oxygenRating * scrubberRating
    }


    val input = File("inputs/2021/3.txt").readLines()
    println(part1(input))
    println(part2(input))
}