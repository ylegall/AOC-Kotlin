package aoc2020

import java.io.File

private object Day9 {

    private fun isValidNumber(current: Long, previous: Set<Long>): Boolean {
        return previous.any { item ->
            item != current && (current - item in previous)
        }
    }

    fun firstInvalidNumber(preambleSize: Int, numbers: List<Long>): Long {
        var first = 0
        var currentIndex = preambleSize
        val previous = numbers.take(preambleSize).toMutableSet()
        while (currentIndex < numbers.size) {
            val curr = numbers[currentIndex]
            if (!isValidNumber(curr, previous)) {
                return curr
            }
            previous.remove(numbers[first])
            previous.add(curr)
            first++
            currentIndex++
        }
        throw Exception("invalid number not found")
    }

    fun findRangeSum(targetSum: Long, numbers: List<Long>): Long {
        val cummulativeSums = numbers.scan(0L) { a, b -> a + b }
        var i = 0
        var j = 1
        while (true) {
            val sum = cummulativeSums[j + 1] - cummulativeSums[i]
            if (sum > targetSum) {
                if (i < j - 1) {
                    i++
                } else {
                    throw Exception("not found")
                }
            } else if (sum < targetSum) {
                if (j < numbers.size - 1) {
                    j++
                } else {
                    throw Exception("not found")
                }
            } else {
                val sorted = numbers.subList(i, j + 1).sorted()
                return sorted.first() + sorted.last()
            }
        }
    }
}

fun main() {
    val numbers = File("inputs/2020/9.txt").useLines { lines -> lines.map { it.toLong() }.toList() }
    val invalid = Day9.firstInvalidNumber(25, numbers)
    println(invalid)
    val sum = Day9.findRangeSum(invalid, numbers)
    println(sum)
}