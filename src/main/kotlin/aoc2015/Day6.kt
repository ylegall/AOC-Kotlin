package aoc2015

import util.Point
import util.input

private const val LEN = 1000

private val pointPattern = Regex("""(\d+),(\d+)""")

private fun extractPoints(str: String): List<Point> {
    return pointPattern.findAll(str).map {
        Point(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt())
    }.toList()
}

private fun Array<IntArray>.set(value: Int, p1: Point, p2: Point) {
    for (row in p1.y .. p2.y) {
        for (col in p1.x .. p2.x) {
            this[row][col] = value
        }
    }
}

private fun Array<IntArray>.add(value: Int, p1: Point, p2: Point, mod2: Boolean) {
    for (row in p1.y .. p2.y) {
        for (col in p1.x .. p2.x) {
            if (mod2) {
                this[row][col] = (Math.max(this[row][col] + value, 0) % 2)
            } else {
                this[row][col] = Math.max(this[row][col] + value, 0)
            }
        }
    }
}

fun main() {
    val lights = Array(LEN) { IntArray(LEN) }
    input("inputs/2015/6.txt").use {
        it.forEach {
            val points = extractPoints(it)
            when {
                it.startsWith("toggle") -> lights.add(1, points[0], points[1], true)
                it.startsWith("turn on") -> lights.set(1, points[0], points[1])
                it.startsWith("turn off") -> lights.set(0, points[0], points[1])
                else -> throw Exception("unknown command: $it")
            }
        }
    }
    println(lights.map { it.sum() }.sum())

    lights.forEach { it.fill(0) }
    input("inputs/2015/6.txt").use {
        it.forEach {
            val points = extractPoints(it)
            when {
                it.startsWith("toggle") -> lights.add(2, points[0], points[1], false)
                it.startsWith("turn on") -> lights.add(1, points[0], points[1], false)
                it.startsWith("turn off") -> lights.add(-1, points[0], points[1], false)
                else -> throw Exception("unknown command: $it")
            }
        }
    }
    println(lights.map { it.sum() }.sum())
}