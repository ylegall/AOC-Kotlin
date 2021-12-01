package aoc2019

import util.Point
import java.io.File

private typealias Directions = List<Pair<Char, Int>>

private object Day3 {

    fun parseInput() = File("inputs/2019/3.txt").readLines().map { line ->
        line.split(",").map { it.first() to it.drop(1).toInt() }
    }

    private fun Point.moveBy(dir: Char) = when(dir) {
        'U' -> Point(x, y - 1)
        'D' -> Point(x, y + 1)
        'R' -> Point(x + 1, y)
        'L' -> Point(x - 1, y)
        else -> throw Exception("invalid direction: $dir")
    }

    private fun pathPoints(directions: Directions): Map<Point, Int> {
        var pos = Point(0, 0)
        val seenPoints = HashMap<Point, Int>()
        var steps = 0
        for ((dir, len) in directions) {
            repeat (len) {
                pos = pos.moveBy(dir)
                steps += 1
                seenPoints.putIfAbsent(pos, steps)
            }
        }
        return seenPoints
    }

    private fun findIntersections(paths: List<Directions>): Set<Pair<Point, Int>> {
        val points1 = pathPoints(paths[0])
        val points2 = pathPoints(paths[1])
        return points1.filter {
            it.key in points2
        }.map { (point, steps) ->
            point to steps + (points2[point] ?: 0)
        }.toSet()
    }

    fun findClosestIntersection(paths: List<Directions>) {
        val intersections = findIntersections(paths)
        println(intersections.map { (point, _) -> point.mDist(0, 0) }.minOrNull())
    }

    fun findfewestStepsToIntersection(paths: List<Directions>) {
        val intersections = findIntersections(paths)
        println(intersections.minByOrNull { it.second }?.second)
    }

}

fun main() {
    val paths = Day3.parseInput()
    Day3.findClosestIntersection(paths)
    Day3.findfewestStepsToIntersection(paths)
}
