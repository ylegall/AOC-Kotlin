package aoc2018

import util.UnionFind
import java.io.File
import kotlin.math.abs

private object Day25 {

    private data class Point4(
            val a: Int,
            val b: Int,
            val c: Int,
            val d: Int
    ) {
        fun distanceTo(other: Point4) = abs(a - other.a) + abs(b - other.b) + abs(c - other.c) + abs(d - other.d)
    }

    private fun parseInput() = File("inputs/2018/25.txt").useLines { lines ->
        lines.map { line ->
            val tokens = line.split(",").map { it.toInt() }
            Point4(tokens[0], tokens[1], tokens[2], tokens[3])
        }.toList()
    }

    fun countConstellations() {
        val points = parseInput()
        val constellations = UnionFind(points.size)
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                if (points[i].distanceTo(points[j]) <= 3) {
                    constellations.union(i, j)
                }
            }
        }
        println(constellations.size)
    }
}

fun main() {
    Day25.countConstellations()
}
