package aoc2017

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private val initialBlocks = listOf(0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11)

private fun uniqueRounds(): Pair<Int, Int> {
    var blocks = initialBlocks.toList()
    var rounds = 0
    val seen = mutableMapOf<List<Int>, Int>()
    while (blocks !in seen) {
        seen[blocks] = rounds
        blocks = blocks.redistribute()
        rounds++
    }
    return rounds to rounds - seen[blocks]!!
}

private fun List<Int>.redistribute(): List<Int> {
    val (idx, maxvalue) = mapIndexed { idx, value -> idx to value }.maxBy { it.second }!!
    val newBlocks = this.toMutableList()
    newBlocks[idx] = 0
    var i = idx + 1
    repeat(maxvalue) {
        newBlocks[i % size] += 1
        i++
    }
    return newBlocks
}

@ExperimentalTime
fun main() {
    println(measureTimedValue { uniqueRounds() })
}