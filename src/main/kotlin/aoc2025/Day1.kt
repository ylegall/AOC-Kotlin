package aoc2025

import java.io.File

private object Day1 {

    const val DIAL_SIZE = 100
    const val START_POSITION = 50

    fun parseInput() = File("input.txt").useLines { lines ->
        lines.map { line ->
            val delta = line.drop(1).toInt()
            if (line.startsWith('L')) -delta else delta
        }.toList()
    }

    fun part1(deltas: List<Int>) {
        val password = deltas.scan(START_POSITION) { position, delta ->
            (position + delta) % DIAL_SIZE
        }.count { it == 0 }
        println(password)
    }

    fun part2(deltas: List<Int>) {
        var count = 0
        var pos = START_POSITION
        for (delta in deltas) {
            val (newDelta, dist) = if (delta < 0) {
                -delta to if (pos == 0) 100 else pos
            } else {
                delta to (100 - pos)
            }
            if (newDelta >= dist) {
                count += (1 + (newDelta-dist)/DIAL_SIZE)
            }
            pos = (pos + delta).mod(DIAL_SIZE)
        }
        println(count)
    }
}

fun main() {
    val rotations = Day1.parseInput()
    Day1.part1(rotations)
    Day1.part2(rotations)
}