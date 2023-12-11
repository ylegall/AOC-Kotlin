package aoc2023

import util.Point
import util.findPoints
import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("input.txt").readLines().map { it.toList() }
    val galaxies = input.findPoints { it == '#' }.toList()

    fun totalDistances(delta: Long): Long {
        val galaxyRows = galaxies.mapTo(mutableSetOf()) { it.y }
        val galaxyCols = galaxies.mapTo(mutableSetOf()) { it.x }
        val emptyRows = input.indices.filter { it !in galaxyRows }
        val emptyCols = input[0].indices.filter { it !in galaxyCols }
        val cumulativeEmptyRows = input.indices.scan(0L) { sum, row -> if (row in emptyRows) sum + 1L else sum }
        val cumulativeEmptyCols = input[0].indices.scan(0L) { sum, col -> if (col in emptyCols) sum + 1L else sum }

        fun expandedDistance(p1: Point, p2: Point): Long {
            val addedRows = abs(cumulativeEmptyRows[p2.y] - cumulativeEmptyRows[p1.y]) * delta
            val addedCols = abs(cumulativeEmptyCols[p2.x] - cumulativeEmptyCols[p1.x]) * delta
            return abs(p2.x - p1.x) + abs(p2.y - p1.y) + addedRows + addedCols
        }

        return (0 until galaxies.size-1).sumOf { i ->
            (i+1 until galaxies.size).sumOf { j ->
                expandedDistance(galaxies[i], galaxies[j])
            }
        }
    }

    fun part1() {
        println(totalDistances(1))
    }

    fun part2() {
        println(totalDistances(999999))
    }

    part1()
    part2()
}