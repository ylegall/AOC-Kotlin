package aoc2025

import util.Point
import util.sign
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private object Day9 {

    data class Edge(
        val p1: Point,
        val p2: Point
    ) {

        fun intersects(other: Edge): Boolean {
            return if (p1.x == p2.x) { // vertical
                if (other.p2.x == other.p1.x) {
                    false
                } else {
                    (sign(other.p1.x - p1.x) != sign(other.p2.x - p1.x)) &&
                            other.p1.y in min(p1.y, p2.y) .. max(p1.y, p2.y)
                }
            } else { // horizontal
                if (other.p2.y == other.p1.y) {
                    false
                } else {
                    (sign(other.p1.y - p1.y) != sign(other.p2.y - p1.y)) &&
                            other.p1.x in min(p1.x, p2.x) .. max(p1.x, p2.x)
                }
            }
        }
    }

    data class Rect(
        val p1: Point,
        val p2: Point
    ) {
        fun area(): Long {
            return (abs(p2.x - p1.x) + 1L) * (abs(p2.y - p1.y) + 1L)
        }

        fun shrink(): Rect {
            val minX = min(p1.x, p2.x)+1
            val minY = min(p1.y, p2.y)+1
            val maxX = max(p1.x, p2.x)-1
            val maxY = max(p1.y, p2.y)-1
            return Rect(Point(minX, minY), Point(maxX, maxY))
        }

        fun edges() = listOf(
            Edge(p1, Point(p2.x, p1.y)),
            Edge(Point(p2.x, p1.y), p2),
            Edge(p2, Point(p1.x, p2.y)),
            Edge(Point(p1.x, p2.y), p1),
        )
    }

    fun parseInput() = File("input.txt").readLines()
        .map { line -> line.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }

    fun part1(points: List<Point>) {
        var maxArea = 0L
        for (i in 1 until points.size-1) {
            for (j in i+1 until points.size) {
                val area = Rect(points[i], points[j]).area()
                maxArea = max(maxArea, area)
            }
        }
        println(maxArea)
    }

    fun makePath(points: List<Point>): List<Point> {
        val pointsByRow = points.groupBy { it.y }
        val pointsByCol = points.groupBy { it.x }
        var currentPoint = points.first()
        val seen = mutableSetOf<Point>()
        val pathPoints = mutableListOf<Point>()

        while (true) {
            seen.add(currentPoint)
            pathPoints.add(currentPoint)
            currentPoint = (
                    (pointsByCol[currentPoint.x] ?: emptyList()) +
                    (pointsByRow[currentPoint.y] ?: emptyList())
            ).firstOrNull { it !in seen } ?: break
        }
        return pathPoints
    }

    fun part2(points: List<Point>) {
        val path = makePath(points)
        val pathEdges = (path + path.first()).windowed(2).map { Edge(it[0], it[1]) }

        val rects = mutableListOf<Rect>()
        for (i in 1 until points.size-1) {
            for (j in i+1 until points.size) {
                rects.add(Rect(points[i], points[j]))
            }
        }
        val biggestRect = rects.sortedByDescending { it.area() }
            .first { rect ->
                rect.shrink().edges().none { edge ->
                    pathEdges.any { edge.intersects(it) }
                }
            }

        println(biggestRect.area())
    }
}

fun main() {
    val points = Day9.parseInput()
    Day9.part1(points)
    Day9.part2(points)
}