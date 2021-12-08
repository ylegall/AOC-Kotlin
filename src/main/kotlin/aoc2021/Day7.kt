package aoc2021

import java.io.File
import java.lang.Math.abs

fun main() {

    fun triangleNumber(x: Int): Int {
        return x * (x + 1) / 2
    }

    fun median(items: List<Int>): Int {
        return items.sorted()[items.size / 2]
    }

    fun average(items: List<Int>): Int {
        return items.sum() / items.size
    }

    fun part1(input: List<Int>): Int {
        val middle = median(input)
        return listOf(middle, middle + 1).minOf { mid ->
            input.sumOf { abs(it - mid) }
        }
    }

    fun part2(input: List<Int>): Int {
        val middle = average(input)
        return listOf(middle, middle + 1).minOf { mid ->
            input.sumOf { triangleNumber(abs(it - mid)) }
        }
    }

    val input = File("inputs/2021/7.txt").useLines { lines ->
        lines.first().split(",").map { it.toInt() }
    }

    println(part1(input))
    println(part2(input))
}