package util

import kotlin.math.abs

data class Point(
        val x: Int,
        val y: Int
): Comparable<Point> {
    override fun compareTo(other: Point) = pointComparator.compare(this, other)
}

val pointComparator = compareBy<Point>({ it.y }, { it.x })

fun Point.mDist(point: Point) = mDist(point.x, point.y)

fun Point.mDist(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

operator fun Point.times(magnitude: Int) = Point(x * magnitude, y * magnitude)