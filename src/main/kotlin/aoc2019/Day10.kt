package aoc2019

import util.Point
import util.gcd
import java.io.File
import kotlin.math.abs
import kotlin.math.atan2


private object Day10 {

    private val grid   = parseInput()

    private fun slopeBetween(p1: Point, p2: Point): Point {
        val delta = p2 - p1
        val commonDivisor = abs(gcd(delta.x, delta.y))
        return Point(delta.x / commonDivisor, delta.y / commonDivisor)
    }

    private fun getAsteroids() = grid.indices.flatMap { y ->
        grid[y].indices.map { x ->
            Point(x, y)
        }.filter { grid[it.y][it.x] == '#' }
    }.toSet()

    private fun findMaxVisibleAsteroids(): Pair<Point, Int> {
        val asteroids = getAsteroids()
        return asteroids.map { first ->
            first to asteroids.minus(first).map { second ->
                slopeBetween(first, second)
            }.distinct().size
        }.maxByOrNull { it.second }!!
    }

    private fun angle(p1: Point, p2: Point): Double {
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y
        return Math.PI - atan2(dx.toDouble(), dy.toDouble())
    }

    private fun find200thAsteroidToBeVaporized(laserPosition: Point): Int {
        val asteroidsByAngle = getAsteroids().minus(laserPosition).groupBy {
            angle(laserPosition, it)
        }.entries.sortedBy { it.key }

        val vaporized = mutableSetOf<Point>()

        var i = 0
        repeat(200) {
            val nextTarget = asteroidsByAngle[i].value.firstOrNull { it !in vaporized }
            nextTarget?.let { vaporized.add(it) }
            i = (i + 1) % asteroidsByAngle.size
        }
        val last = vaporized.last()
        println(last)
        return last.x * 100 + last.y
    }

    private fun parseInput() = File("inputs/2019/10.txt").readLines()

    private fun List<String>.print() = forEach { println(it) }

    fun run() {
        val (bestPoint, visibleCount) = findMaxVisibleAsteroids()
        println("best point = $bestPoint and has $visibleCount visible neighbors")
        println(find200thAsteroidToBeVaporized(bestPoint))
    }

}

fun main() {
    Day10.run()
}