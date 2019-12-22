package aoc2019

import util.repeat
import java.io.File
import kotlin.math.abs

private val basePattern = sequenceOf(0, 1, 0, -1)

private fun getPatternCoefficients(outputIndex: Int): Sequence<Int> = basePattern
        .repeat()
        .flatMap { item ->
            generateSequence { item }.take(outputIndex + 1)
        }.drop(1)

private fun computePhase(input: List<Int>): List<Int> {
    return input.indices.map { index ->
        val sum = getPatternCoefficients(index)
                .zip(input.asSequence())
                .take(input.size).map {
                    it.first * it.second
                }.sum()
        abs(sum % 10)
    }.toList()
}

fun computeFirst8DigitsAfter100Phases(originalInput: List<Int>) {
    var input = originalInput
    repeat(100) { input = computePhase(input) }
    val first8Digits = input.take(8).joinToString("")
    println(first8Digits)
}

private fun parseInput() = File("inputs/2019/16.txt").readText().trim().map { it - '0' }

fun main() {
    val input = parseInput()
    computeFirst8DigitsAfter100Phases(input)
}