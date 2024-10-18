package aoc2019

import util.Point
import java.io.File

fun main() {

    class Path(val segments: List<Pair<Char, Int>>)

    fun parseInput(filename: String): List<Path> {
        return File(filename).readLines().map { line ->
            val segments = line.split(",").map { it.first() to it.drop(1).toInt() }
            Path(segments)
        }
    }

    fun extendFromPoint(pos: Point, dir: Char, steps: Int): List<Point> {
        return when (dir) {
            'U' -> (pos.y - 1 downTo pos.y - steps).map { Point(pos.x, it) }
            'D' -> (pos.y + 1 .. pos.y + steps).map { Point(pos.x, it) }
            'R' -> (pos.x + 1 .. pos.x + steps).map { Point(it, pos.y) }
            'L' -> (pos.x - 1 downTo pos.x - steps).map { Point(it, pos.y) }
            else -> throw Exception("invalid direction: $dir")
        }
    }

    fun pathPoints(path: List<Pair<Char, Int>>): Map<Point, Int> {
        var pos = Point(0, 0)
        val seenPoints = mutableMapOf<Point, Int>()
        var steps = 0
        for ((dir, len) in path) {
            val nextPoints = extendFromPoint(pos, dir, len)
            for (point in nextPoints) {
                steps += 1
                if (point !in seenPoints) {
                    seenPoints[point] = steps
                }
            }
            pos = nextPoints.last()
        }
        return seenPoints
    }

    fun findClosestIntersection(paths: List<Path>) {
        val points1 = pathPoints(paths[0].segments)
        val points2 = pathPoints(paths[1].segments)
        val intersections = points1.keys.filter {
            it in points2
        }
        println(intersections.minOf { it.mDist(0, 0) })
    }

    fun findfewestStepsToIntersection(paths: List<Path>) {
        val points1 = pathPoints(paths[0].segments)
        val points2 = pathPoints(paths[1].segments)
        val intersections = points1.filter {
            it.key in points2
        }.map { (point, steps) ->
            point to steps + (points2[point] ?: 0)
        }.toSet()
        println(intersections.minOf { it.second } )
    }

    val directions = parseInput("input.txt")
    findClosestIntersection(directions)
    findfewestStepsToIntersection(directions)
}
