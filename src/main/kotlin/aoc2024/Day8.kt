package aoc2024

import util.Point
import java.io.File

fun main() {

    class Input(
        val grid: List<String>,
        val nodes: Map<Char, List<Point>>
    )

    fun parseInput(): Input {
        val nodes = mutableMapOf<Char, MutableList<Point>>()
        val grid = File("input.txt").readLines()
        grid.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c.isDigit() || c.isLetter()) {
                    val positions = nodes.getOrPut(c) { mutableListOf() }
                    positions.add(Point(row, col))
                }
            }
        }
        return Input(grid, nodes)
    }

    val input = parseInput()

    fun findAntinodes(a: Point, b: Point): List<Point> {
        val dx = b.x - a.x
        val dy = b.y - a.y
        return listOf(
            Point(b.x + dx, b.y + dy),
            Point(a.x - dx, a.y - dy),
        )
    }

    fun part1() {
        val antiPositions = mutableSetOf<Point>()
        input.nodes.forEach { _, positions ->
            for (i in 0 until positions.size-1) {
                for (j in i + 1 until positions.size) {
                    antiPositions.addAll(
                        findAntinodes(positions[i], positions[j]).filter {
                            it.x in 0 until input.grid[0].length &&
                            it.y in 0 until input.grid.size
                        }
                    )
                }
            }
        }
        println(antiPositions.size)
    }

    fun Point.onLine(a: Point, b: Point): Boolean {
        val det = (b.x - a.x) * (this.y - a.y) - (b.y - a.y) * (this.x - a.x)
        return det == 0
    }

    fun part2() {
        val lines = mutableMapOf<Char, MutableList<Pair<Point, Point>>>()
        input.nodes.forEach { (freq, positions) ->
            for (i in 0 until positions.size - 1) {
                for (j in i + 1 until positions.size) {
                    val line = positions[i] to positions[j]
                    val freqLines = lines.getOrPut(freq) { mutableListOf() }
                    freqLines.add(line)
                }
            }
        }

        var uniqueCount = 0
        val antiLocations = mutableSetOf<Point>()
        for (row in input.grid.indices) {
            for (col in input.grid[row].indices) {
                val pos = Point(col, row)
                for (freq in input.nodes.keys) {
                    for (line in (lines[freq] ?: emptyList())) {
                        if (pos.onLine(line.first, line.second)) {
                            antiLocations.add(pos)
                            uniqueCount++
                        }
                    }
                }
            }
        }
        println(antiLocations.size)
    }

    part1()
    part2()
}