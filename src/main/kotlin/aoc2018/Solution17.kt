package aoc2018

import util.input
import java.lang.IllegalStateException
import kotlin.streams.asSequence

private val inputPattern = Regex("""([xy])=(\d+), ([xy])=(\d+)..(\d+)""")

private fun parseInputLine(line: String): Iterable<Pair<Int, Int>> {
    val match = inputPattern.matchEntire(line) ?: throw IllegalStateException("bad input line: $line")

    val isHorizontal = match.groupValues[1] == "y"
    val staticDimension = match.groupValues[2].toInt()
    val dynamicRange = IntRange(match.groupValues[4].toInt(), match.groupValues[5].toInt())

    return dynamicRange.map { value ->
        if (isHorizontal) {
            value to staticDimension
        } else {
            staticDimension to value
        }
    }
}

fun main() {
    input("inputs/2018/17.txt").use {
        it.asSequence().forEach { line ->
            println(parseInputLine(line))
        }
    }
}