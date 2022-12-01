package aoc2022

import util.split
import java.io.File

fun main() {

    fun parseInput(): List<Int> {
        return File("input.txt").readLines()
                .split { it.isEmpty() }
                .map { chunk -> chunk.sumOf { it.toInt() } }
    }

    fun part1() {
        val elfCalories = parseInput()
        println(elfCalories.maxOrNull())
    }

    fun part2() {
        val elfCalories = parseInput()
        println(elfCalories.sorted().takeLast(3).sum())
    }

    part1()
    part2()
}