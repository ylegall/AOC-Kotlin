package aoc2015

import java.io.File

fun main() {

    fun charValue(char: Char) = if (char == '(') 1L else -1

    fun part1() {
        File("input.txt").readText().sumOf { charValue(it) }.also { println(it) }
    }

    fun part2() {
        val result = File("input.txt").readText().asSequence()
            .scan(0L) { acc, char ->
                acc + charValue(char)
            }.withIndex()
            .first { it.value == -1L }
        println(result.index)
    }

    part1()
    part2()
}