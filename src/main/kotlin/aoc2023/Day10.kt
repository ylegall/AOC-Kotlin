package aoc2023

import util.Point
import util.findFirstPoint
import util.get
import java.io.File

fun main() {

    fun validNeighbors(pos: Point, grid: List<String>): List<Point> {
        return when (val ch = grid[pos]) {
            '|' -> listOf(pos + Point(0, 1), pos - Point(0, 1))
            '-' -> listOf(pos + Point(1, 0), pos - Point(1, 0))
            'L' -> listOf(pos - Point(0, 1), pos + Point(1, 0))
            'J' -> listOf(pos - Point(0, 1), pos - Point(1, 0))
            '7' -> listOf(pos + Point(0, 1), pos - Point(1, 0))
            'F' -> listOf(pos + Point(0, 1), pos + Point(1, 0))
            '.' -> emptyList()
            else -> throw Exception("bad tile: '$ch', point=$pos")
        }
    }

    fun connectingNeighbors(pos: Point, grid: List<String>): List<Point> {
        return pos.cardinalNeighbors()
            .filter { it.x in grid.first().indices && it.y in grid.indices }
            .mapNotNull { np ->
                val neighbors = validNeighbors(np, grid)
                if (pos in neighbors) np else null
            }
    }

    fun nextPointFromTile(pos: Point, last: Point, input: List<String>): Point {
        return validNeighbors(pos, input).first { it != last }
    }

    fun tracePath(start: Point, input: List<String>): List<Point> {
        val neighbors = connectingNeighbors(start, input)
        var current = neighbors[0]
        var last = start
        val path = mutableListOf(start)
        while (current != start) {
            path.add(current)
            val next = nextPointFromTile(current, last, input)
            last = current
            current = next
        }
        return path
    }

    fun part1() {
        val input = File("input.txt").readLines()
        val start = input.findFirstPoint('S')!!
        val path = tracePath(start, input)
        println(path.size/2)
    }

    fun part2() {
        val input = File("input.txt").readLines()
        val start = input.findFirstPoint('S')!!
        val pathPoints = tracePath(start, input).toSet()

        var innerCount = 0
        for (row in input.indices) {
            var inside = false
            for (col in input[row].indices) {
                val pos = Point(col, row)
                if (pos in pathPoints) {
                    if (input[pos] in "|F7") {
                        inside = !inside
                    }
                } else {
                    if (inside) {
                        innerCount += 1
                    }
                }
            }
        }
        println(innerCount)
    }

    part1()
    part2()
}
