package aoc2017

import util.input
import kotlin.streams.toList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private fun jumpsUntilExit(input: IntArray): Int {
    var jumps = 0
    var idx = 0
    while (idx < input.size) {
        val nextOffset = input[idx]
        input[idx] += 1
        idx += nextOffset
        jumps++
    }
    return jumps
}

private fun jumpsUntilExit2(input: IntArray): Int {
    var jumps = 0
    var idx = 0
    while (idx in input.indices) {
        val nextOffset = input[idx]
        input[idx] += if (nextOffset >= 3) -1 else 1
        idx += nextOffset
        jumps++
    }
    return jumps
}

@ExperimentalTime
fun main() {
    val jumps = input("inputs/2017/5.txt").use { lines ->
        lines.map { it.toInt() }.toList()
    }

    println( measureTimedValue { jumpsUntilExit(jumps.toIntArray()) })

    println( measureTimedValue { jumpsUntilExit2(jumps.toIntArray()) })
}