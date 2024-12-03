package aoc2024

import java.io.File
import kotlin.math.abs

fun main() {

    val reports = File("input.txt").readLines().map { line ->
        line.split(" ").map { it.toInt() }
    }

    fun List<Int>.isValid(): Boolean {
        val deltas = this.zipWithNext().map { it.second - it.first }
        val increasing = deltas.all { it > 0 }
        val decreasing = deltas.all { it < 0 }
        val minDiff = deltas.minOf { abs(it) }
        val maxDiff = deltas.maxOf { abs(it) }
        return (increasing || decreasing) && minDiff >= 1 && maxDiff <= 3
    }

    fun List<Int>.expanded() = indices.map { idx ->
        this.take(idx) + this.takeLast(this.size - 1 - idx)
    }

    fun List<Int>.isValid2(): Boolean {
        return this.expanded().any { it.isValid() }
    }

    fun part1() {
        println(reports.count { it.isValid() })
    }

    fun part2() {
        println(reports.count { it.isValid2() })
    }

    part1()
    part2()
}