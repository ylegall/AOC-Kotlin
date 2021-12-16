package aoc2021

import util.Point
import java.io.File
import java.util.*

fun main() {

    class State(
        val pos: Point,
        val totalRisk: Int = 0,
    ): Comparable<State> {
        override fun compareTo(other: State): Int {
            return if (totalRisk == other.totalRisk) {
                (other.pos.x + other.pos.y) - (pos.x + pos.y)
            } else {
                totalRisk - other.totalRisk
            }
        }
    }

    fun part1(grid: List<IntArray>): Int {
        val start = State(Point(0, 0))
        val goalPosition = Point(grid.size - 1, grid.last().size - 1)
        val seen = mutableSetOf<Point>()
        val q = PriorityQueue<State>()
        q.add(start)

        while (q.isNotEmpty()) {
            val current = q.poll()
            if (current.pos == goalPosition) {
                return current.totalRisk
            } else if (current.pos !in seen) {
                current.pos.cardinalNeighbors().asSequence()
                    .filter { it !in seen }
                    .filter { it.x in grid[0].indices && it.y in grid.indices }
                    .forEach { newPosition ->
                        val newRisk = grid[newPosition.y][newPosition.x]
                        q.add(State(newPosition, current.totalRisk + newRisk))
                    }
                seen.add(current.pos)
            }
        }
        return -1
    }

    fun List<IntArray>.getRiskLevel(position: Point): Int {
        val tileRow = position.y / this.size
        val tileCol = position.x / this[0].size
        val baseValue = this[position.y % this.size][position.x % this[0].size]
        return ((baseValue - 1 + tileRow + tileCol) % 9) + 1
    }

    fun part2(grid: List<IntArray>): Int {
        val start = State(Point(0, 0))
        val goalPosition = Point(grid.size * 5 - 1, grid.last().size * 5 - 1)
        val seen = mutableSetOf<Point>()
        val q = PriorityQueue<State>()
        q.add(start)
        while (q.isNotEmpty()) {
            val current = q.poll()
            if (current.pos == goalPosition) {
                return current.totalRisk
            } else if (current.pos !in seen) {
                seen.add(current.pos)
                current.pos.cardinalNeighbors().asSequence()
                    .filter { it !in seen }
                    .filter { it.x in 0 until 5 * grid[0].size && it.y in 0 until 5 * grid.size }
                    .forEach { newPosition ->
                        val newRisk = grid.getRiskLevel(newPosition)
                        q.add(State(newPosition, current.totalRisk + newRisk))
                    }
            }
        }
        return -1
    }

    val grid = File("inputs/2021/input.txt").useLines { lines ->
        lines.map { line ->
            line.map { it.digitToInt() }.toIntArray()
        }.toList()
    }

    println(part1(grid))
    println(part2(grid))
}