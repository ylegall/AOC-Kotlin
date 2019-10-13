package aoc2017

import util.Point
import java.io.File
import kotlin.math.abs
import kotlin.math.max

private object Day11 {

    private val origin = Point(0, 0)

    private fun Point.distanceTo(other: Point): Int {
        val z = -x - y
        val oz = -other.x - other.y
        return max(abs(x - other.x), max(abs(y - other.y), abs(z - oz)))
    }

    fun findHexDistance(directions: String) {
        var maxDist = 0
        var r = 0
        var c = 0
        for (dir in directions.split(",")) {
            when (dir) {
                "n"  -> { r -= 1 }
                "nw" -> { c -= 1 }
                "ne" -> { r -=1; c += 1 }
                "s"  -> { r += 1 }
                "sw" -> { r += 1; c -= 1 }
                "se" -> { c += 1 }
                else -> throw Exception("invalid direction: $dir")
            }
            maxDist = max(maxDist, Point(r, c).distanceTo(origin))
        }
        println("max distance: $maxDist")
        println("final distance: " + (Point(r, c).distanceTo(origin)))
    }

}

fun main() {
    val input = File("inputs/2017/11.txt").readText().trim()
    Day11.findHexDistance(input)
}
