package aoc2025

import util.Point
import java.io.File
import kotlin.collections.count

private object Day4 {
    fun parseInput(): List<CharArray> {
        return File("input.txt").readLines()
            .map { it.toCharArray() }
    }

    fun Point.adjacentPositions() = listOf(
        Point(x + 1, y),
        Point(x + 1, y + 1),
        Point(x, y + 1),
        Point(x - 1, y + 1),
        Point(x - 1, y),
        Point(x - 1, y - 1),
        Point(x, y - 1),
        Point(x + 1, y - 1),
    )

    operator fun List<CharArray>.get(point: Point) =
        getOrNull(point.y)?.getOrNull(point.x)

    fun List<CharArray>.isAccessible(point: Point): Boolean {
        return this[point] == '@' && point.adjacentPositions()
                    .mapNotNull { this[it] }
                    .count { it == '@' } < 4
    }

    fun accessiblePositions(grid: List<CharArray>): List<Point> {
        val positions = mutableListOf<Point>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val pos = Point(col, row)
                if (grid.isAccessible(pos)) {
                    positions.add(pos)
                }
            }
        }
        return positions
    }

    fun countAccessibleRolls(grid: List<CharArray>): Int {
        return accessiblePositions(grid).size
    }

    fun part1(input: List<CharArray>) {
        println(countAccessibleRolls(input))
    }

    fun part2(input: List<CharArray>) {
        var totalRemoved = 0
        val grid = input.map { it.copyOf() }.toList()
        do {
            val positions = accessiblePositions(grid)
            for (position in positions) {
                grid[position.y][position.x] = '.'
            }
            totalRemoved += positions.size
        } while (positions.isNotEmpty())
        println(totalRemoved)
    }
}

fun main() {
    val input = Day4.parseInput()
    Day4.part1(input)
    Day4.part2(input)
}