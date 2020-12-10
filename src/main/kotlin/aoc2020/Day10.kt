package aoc2020

import java.io.File

private fun countJoltDifferencesWithAllAdapters(input: List<Int>): Int {
    val adapters = listOf(0) + input
    val joltDifferenceCounts = Array(4) { 0 }
    for (i in 1 until adapters.size) {
        val delta = adapters[i] - adapters[i - 1]
        joltDifferenceCounts[delta]++
    }
    joltDifferenceCounts[3]++
    return joltDifferenceCounts[1] * joltDifferenceCounts[3]
}

private fun countAdapterCombinations(input: List<Int>): Long {
    val adapters = listOf(0) + input
    val counts = Array(adapters.size) { 0L }
    counts[0] = 1L
    for (i in 1 until counts.size) {
        for (j in 1 .. 3) {
            val prev = i - j
            if (prev >= 0 && (adapters[i] - adapters[prev]) <= 3) {
                counts[i] += counts[i - j]
            }
        }
    }
    return counts[counts.size - 1]
}

fun main() {
    val adapters = File("inputs/2020/10.txt").readLines().map { it.toInt() }.sorted()
    println(countJoltDifferencesWithAllAdapters(adapters))
    println(countAdapterCombinations(adapters))
}