package aoc2023

import util.repeat
import java.io.File

fun main() {

    fun List<Int>.deltas() = zipWithNext().map { (a, b) -> b - a }

    fun extrapolate(items: List<Int>, forward: Boolean = true): Int {
        val lastItems = sequenceOf(items)
            .repeat()
            .scan(items) { acc, _ -> acc.deltas() }
            .takeWhile { it.any { it != 0 } }
            .map { if (forward) it.last() else it.first() }
            .toList()
        val placeHolders = lastItems.reversed().scan(0) { acc, next ->
            if (forward) acc + next else next - acc
        }
        return placeHolders.last()
    }

    fun part1() {
        val result = File("input.txt").useLines { lines ->
            lines.sumOf { line ->
                val values = line.split(" ").map { it.toInt() }
                extrapolate(values, forward = true)
            }
        }
        println(result)
    }

    fun part2() {
        val result = File("input.txt").useLines { lines ->
            lines.sumOf { line ->
                val values = line.split(" ").map { it.toInt() }
                extrapolate(values, forward = false)
            }
        }
        println(result)
    }

    part1()
    part2()

}