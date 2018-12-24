package aoc2017

import util.input

// part 1
private fun consecutiveDigitSum(line: String): Int {
    val input = line + line[0]
    return line.filterIndexed { i, _ ->
        input[i] == input[i + 1]
    }.map { it - '0' }.sum()
}

// part 2
private fun halfwayMatchSum(line: String): Int {
    val input = line + line
    return line.filterIndexed { i, _ ->
        input[i] == input[i + line.length/2]
    }.map { it - '0' }.sum()
}

fun main() {
    val line = input("inputs/2017/1.txt").use {
        it.findFirst().get().trim()
    }
    println(consecutiveDigitSum(line))
    println(halfwayMatchSum(line))
}