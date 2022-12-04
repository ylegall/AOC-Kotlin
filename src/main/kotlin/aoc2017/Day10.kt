package aoc2017

import util.knotHash
import util.knotHashToString

private fun singleKnotHashIteration(input: List<Int>) {
    val result = knotHash(input, suffix = emptyList(), iterations = 1, condense = false)
    println(result[0] * result[1])
}

private fun fullKnotHash(input: String) {
    val lengths = input.map { it.code }
    val result = knotHashToString(lengths)
    println(result)
}

fun main() {
    singleKnotHashIteration("212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164".split(",").map { it.toInt() })
    fullKnotHash("212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164")
}
