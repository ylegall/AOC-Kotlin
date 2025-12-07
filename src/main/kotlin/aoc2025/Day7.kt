package aoc2025

import util.Direction
import util.Point
import util.get
import util.findFirstPoint
import util.move
import java.io.File

private object Day7 {

    class Input(
        val startPos: Point,
        val grid: List<String>
    )

    fun parseInput(): Input {
        val grid = File("input.txt").readLines()
        val startPos = grid.findFirstPoint('S')!!
        return Input(startPos, grid)
    }

    data class Results(
        val splitPoints: Int,
        val totalPaths: Long
    )

    fun totalPaths(input: Input): Results {
        val cache = mutableMapOf<Point, Long>()
        val splitPoints = mutableSetOf<Point>()

        fun recurse(pos: Point): Long {
            val cachedResult = cache[pos]
            return if (cachedResult != null) {
                cachedResult
            } else {
                when {
                    pos.y >= input.grid.size -> 1L
                    input.grid[pos] == '^' -> {
                        splitPoints.add(pos)
                        recurse(pos.move(Direction.EAST)) +
                            recurse(pos.move(Direction.WEST))
                    }
                    else -> {
                        recurse(pos.move(Direction.SOUTH))
                    }
                }.also {
                    cache[pos] = it
                }
            }
        }

        val totalPaths = recurse(input.startPos)

        return Results(
            splitPoints.size,
            totalPaths
        )
    }
}

fun main() {
    val input = Day7.parseInput()
    val results = Day7.totalPaths(input)
    println(results.splitPoints)
    println(results.totalPaths)
}
