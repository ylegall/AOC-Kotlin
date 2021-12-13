package aoc2021

import java.io.File

fun main() {

    fun part1(depths: List<Int>): Int {
        return depths.windowed(2).count { it[1] > it[0] }
    }

    fun part2(depths: List<Int>): Int {
        val windowSums = depths.windowed(3).map { it.sum() }
        return windowSums.windowed(2).count { it[1] > it[0] }
    }

    val depths = File("inputs/2021/input.txt").useLines { lines -> lines.map { it.toInt() }.toList() }
    println(part1(depths))
    println(part2(depths))
}