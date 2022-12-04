package aoc2022

import java.io.File

fun main() {

    operator fun IntRange.contains(other: IntRange) = first <= other.first && last >= other.last

    fun IntRange.overlaps(other: IntRange) = first in other || other.first in this

    val input = File("input.txt").useLines { lines ->
        lines.map { line ->
            val tokens = line.split("-", ",").map { it.toInt() }
            Pair(IntRange(tokens[0], tokens[1]), IntRange(tokens[2], tokens[3]))
        }.toList()
    }

    fun part1() {
        val fullyContainedPairs = input.count { (range1, range2) ->
            range2 in range1 || range1 in range2
        }
        println(fullyContainedPairs)
    }

    fun part2() {
        val overlappingPairs = input.count { (range1, range2) ->
            range1.overlaps(range2)
        }
        println(overlappingPairs)
    }

    part1()
    part2()
}