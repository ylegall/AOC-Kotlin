package aoc2024

import util.Point
import util.product
import java.io.File

fun main() {

    val spaceSize = Point(101, 103)
    // val spaceSize = Point(11, 7)
    val mid = Point(spaceSize.x / 2, spaceSize.y / 2)

    data class Particle(
        var x: Int,
        var y: Int,
        val vel: Point
    ) {
        fun update(spaceSize: Point) {
            x = (x + vel.x).mod(spaceSize.x)
            y = (y + vel.y).mod(spaceSize.y)
        }

        fun quadrant(): Int? {
            return if (x == mid.x || y == mid.y) {
                null
            } else {
                val qx = if (x < mid.x) 0 else 1
                val qy = if (y < mid.y) 0 else 1
                2 * qx + qy
            }
        }
    }

    fun parseInput() = File("input.txt").readLines().map { line ->
        val tokens = line.drop(2).split(",", " v=").map { it.toInt() }
        Particle(
            tokens[0],
            tokens[1],
            Point(tokens[2], tokens[3])
        )
    }

    fun printParticles(points: Set<Point>) {
        val str = (0 until spaceSize.y).joinToString("\n") { y ->
            (0 until spaceSize.x).joinToString("") { x ->
                val p = Point(x, y)
                if (p in points) "#" else "."
            }
        }
        println(str)
        println()
    }

    fun part1() {
        val particles = parseInput()
        repeat(100) {
            particles.forEach { it.update(spaceSize) }
        }
        val result = particles.groupBy { it.quadrant() }
            .filter { it.key != null }
            .values.map { it.size.toLong() }
            .product()
        println(result)
    }

    fun part2() {
        val particles = parseInput()
        val numParticles = particles.size
        var steps = 0
        while (true) {
            particles.forEach { it.update(spaceSize) }
            steps++
            // printParticles(particles.map { Point(it.x, it.y) }.toSet())
            val overlapCount = numParticles - particles.map { Point(it.x, it.y) }.distinct().size
            if (overlapCount == 0) break
        }
        println(steps)
    }

    part1()
    part2()
}
