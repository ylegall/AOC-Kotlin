package aoc2023

import java.io.File

fun main() {

    val digitWords = listOf("one","two","three","four","five","six","seven","eight","nine")

    val regex = digitWords.let { """(?=(\d|""" + it.joinToString("|") + "))" }
        .also { println("regex: $it") }
        .toRegex()

    val digitMap = digitWords.mapIndexed { idx, digit -> digit to (idx+1) }.toMap()

    fun part1() {
        val sum = File("input.txt").readLines().sumOf { line ->
            val digits = line.filter { it.isDigit() }
            val first = digits.first().digitToInt()
            val last = digits.last().digitToInt()
            first * 10 + last
        }
        println(sum)
    }

    fun String.toDigit() = digitMap[this] ?: this[0].digitToInt()

    fun part2() {
        val sum = File("input.txt").readLines().sumOf { line ->
            val tokens = regex.findAll(line).map { it.groups[1]!!.value }.toList()
            val first = tokens.first().toDigit()
            val last = tokens.last().toDigit()
            first * 10L + last
        }
        println(sum)
    }

    part1()
    part2()
}
