package aoc2019

import util.repeat
import java.io.File
import kotlin.math.abs

private val basePattern = sequenceOf(0, 1, 0, -1)
private val inputList = File("inputs/2019/16.txt").readText().trim().map { it - '0' }
private const val REPEATED_LENGTH = 10000
private val totalLength = inputList.size * REPEATED_LENGTH

private fun getPatternCoefficients(outputIndex: Int) = basePattern
        .repeat()
        .flatMap { item ->
            generateSequence { item }.take(outputIndex + 1)
        }.drop(1)

private fun computePhase(input: List<Int>) = input.indices.asSequence().map { index ->
    getPatternCoefficients(index)
            .zip(input.asSequence())
            .take(input.size).map {
                it.first * it.second
            }.sum().let {
                abs(it) % 10
            }
}.toList()

private fun computeDigits(src: ByteArray, dst: ByteArray) {
    var digitSum: Byte = 0
    var index = dst.size - 1
    while (index >= 0) {
        digitSum = ((digitSum + src[index]) % 10).toByte()
        dst[index] = digitSum
        index--
    }
}

private fun computeFFT(phases: Int = 100) {
    var input = inputList
    repeat(phases) {
        input = computePhase(input)
    }
    val digits = input.take(8).joinToString("")
    println(digits)
}

private fun computeBigOffsetFFT() {
    val digitsToSkip = inputList.take(7).joinToString("").toInt()
    val digits1 = ByteArray(totalLength - digitsToSkip) { (inputList[(digitsToSkip + it) % inputList.size]).toByte() }
    val digits2 = ByteArray(digits1.size) { 0 }

    repeat(50) {
        computeDigits(digits1, digits2)
        computeDigits(digits2, digits1)
    }

    println(digits1.take(8).joinToString(""))
}

fun main() {
    computeFFT()
    computeBigOffsetFFT()
}