package util

import kotlin.math.abs

data class Point(
        val x: Int,
        val y: Int
): Comparable<Point> {

    override fun compareTo(other: Point) = pointComparator.compare(this, other)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun times(magnitude: Int) = Point(x * magnitude, y * magnitude)

    fun mDist(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)

    fun mDist(point: Point) = mDist(point.x, point.y)

    fun cardinalNeighbors() = listOf(
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x + 1, y),
            Point(x, y - 1)
    )
}

val pointComparator = compareBy<Point>({ it.y }, { it.x })

fun enclosingRect(points: Iterable<Point>): Pair<Point, Point> {
    val minX = points.minBy { it.x }!!.x
    val minY = points.minBy { it.y }!!.y
    val maxX = points.maxBy { it.x }!!.x
    val maxY = points.maxBy { it.y }!!.y
    return Point(minX, minY) to Point(maxX, maxY)
}

operator fun List<String>.get(point: Point) = this[point.y][point.x]

inline fun <T> Iterable<Iterable<T>>.findPoint(predicate: (T) -> Boolean): Point? {
    return this.mapIndexed { y, row ->
        row.mapIndexed { x, item ->
            Point(x, y) to item
        }
    }.flatten().firstOrNull { predicate(it.second) }?.first
}