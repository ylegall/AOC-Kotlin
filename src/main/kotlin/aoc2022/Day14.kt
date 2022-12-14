package aoc2022

import util.Point
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {

    val sandSource = Point(500, 0)

    val rocks = File("input.txt").useLines { lines ->
        lines.flatMap { line ->
            line.split(" -> ").zipWithNext().flatMap { (p1, p2) ->
                val (x1, y1) = p1.split(",").map { it.toInt() }
                val (x2, y2) = p2.split(",").map { it.toInt() }
                if (x1 == x2) {
                    (min(y1, y2) .. max(y1, y2)).map { Point(x1, it) }
                } else {
                    (min(x1, x2) .. max(x1, x2)).map { Point(it, y1) }
                }
            }
        }.toSet()
    }

    val rockBottom = rocks.maxBy { it.y }.y + 2
    val minX = rocks.minBy { it.x }.x
    val maxX = rocks.maxBy { it.x }.x

    fun printGrid(sand: Set<Point>) {
        for (row in 0 until rockBottom) {
            for (col in minX..maxX) {
                val c = when (Point(col, row)) {
                    in rocks -> '#'
                    in sand -> 'o'
                    sandSource -> '+'
                    else -> '.'
                }
                print(c)
            }
            println()
        }
    }


    fun simulateSingle(sand: Set<Point>): Point {
        var pos = sandSource
        while (pos.y < rockBottom - 1) {
            val down = Point(pos.x, pos.y + 1)
            val downLeft = Point(pos.x - 1, pos.y +1)
            val downRight = Point(pos.x + 1, pos.y +1)
            pos = when {
                down !in rocks && down !in sand           -> down
                downLeft !in rocks && downLeft !in sand   -> downLeft
                downRight !in rocks && downRight !in sand -> downRight
                else -> break
            }
        }
        return pos
    }

    fun simulate(useFloor: Boolean = false): Int {
        val sand = mutableSetOf<Point>()
        while (true) {
            val restPosition = simulateSingle(sand)
            if (!useFloor) {
                if (restPosition.y >= rockBottom - 1) {
                    printGrid(sand)
                    return sand.size
                }
            } else {
                if (restPosition == sandSource) {
                    printGrid(sand)
                    return sand.size + 1
                }
            }
            sand.add(restPosition)
        }
    }

    fun part1() {
        println(simulate())
    }

    fun part2() {
        println(simulate(useFloor = true))
    }

    part1()
    part2()
}
