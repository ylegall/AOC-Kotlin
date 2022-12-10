package aoc2022

import util.Point
import util.sign
import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("input.txt").useLines { lines ->
        lines.flatMap { line ->
            val (dir, count) = line.split(" ")
            List(count.toInt()) { dir[0] }
        }.toList()
    }

    fun Point.move(dir: Char): Point {
        return when (dir) {
            'U' -> Point(x, y + 1)
            'D' -> Point(x, y - 1)
            'R' -> Point(x + 1, y)
            'L' -> Point(x - 1, y)
            else -> throw Exception("bad dir: $dir")
        }
    }

    fun Point.moveTowards(other: Point): Point {
        val dx = other.x - x
        val dy = other.y - y
        val mdist = abs(dx) + abs(dy)

        return when (dx) {
            0 -> when {
                dy != 0 && mdist > 1 -> Point(x, y + sign(dy))
                else -> this
            }
            else -> when {
                dy != 0 -> if (mdist > 2) Point(x + sign(dx), y + sign(dy)) else this
                else -> if (mdist > 1) Point(x + sign(dx), y) else this
            }
        }
    }

    fun List<Point>.move(dir: Char): List<Point> {
        val head = first().move(dir)
        return drop(1).scan(head) { prev, curr ->
            curr.moveTowards(prev)
        }
    }

    fun simulateRope(ropeSize: Int): Int {
        val initialRope = List(ropeSize) { Point(0, 0) }
        return input.asSequence()
                .scan(initialRope) { rope, dir -> rope.move(dir) }
                .map { it.last() }
                .toSet().size
    }

    fun part1() {
        println(simulateRope(2))
    }

    fun part2() {
        println(simulateRope(10))
    }

    part1()
    part2()
}