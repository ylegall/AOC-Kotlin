package util

import kotlin.math.abs
import kotlin.math.sqrt

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

    fun dist(point: Point): Double {
        val dx = (x - point.x)
        val dy = (y - point.y)
        return sqrt((dx * dx + dy * dy).toDouble())
    }

    fun cardinalNeighbors() = listOf(
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x + 1, y),
            Point(x, y - 1)
    )

    fun mooreNeighbors() = cardinalNeighbors() + listOf(
        Point(x - 1, y - 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1),
        Point(x + 1, y + 1),
    )
}

val pointComparator = compareBy<Point>({ it.y }, { it.x })

fun enclosingRect(points: Iterable<Point>): Pair<Point, Point> {
    val minX = points.minByOrNull { it.x }!!.x
    val minY = points.minByOrNull { it.y }!!.y
    val maxX = points.maxByOrNull { it.x }!!.x
    val maxY = points.maxByOrNull { it.y }!!.y
    return Point(minX, minY) to Point(maxX, maxY)
}

fun List<String>.bounds() = Point(0, 0) to Point(size, this[0].length)

operator fun Pair<Point, Point>.contains(p: Point): Boolean {
    return p.x in (first.x until second.x) &&
            p.y in (first.y until second.y)
}

operator fun List<String>.get(point: Point) = this[point.y][point.x]
operator fun List<String>.get(row: Int, col: Int) = get(row)[col]

operator fun <T> List<List<T>>.get(point: Point) = this[point.y][point.x]
// operator fun <T> List<MutableList<T>>.get(point: Point) = this[point.y][point.x]
operator fun <T> List<MutableList<T>>.set(point: Point, value: T) {
    this[point.y][point.x] = value
}

fun List<String>.getOrNull(row: Int, col: Int) = getOrNull(row)?.getOrNull(col)
fun List<String>.getOrNull(point: Point) = getOrNull(point.y)?.getOrNull(point.x)

fun Iterable<String>.findFirstPoint(char: Char): Point? {
    return this.asSequence()
        .flatMapIndexed { row, line -> line.mapIndexed { col, c -> Point(row, col) to c } }
        .find { it.second == char }?.first
}

inline fun <T> Iterable<Collection<T>>.findPoints(crossinline predicate: (T) -> Boolean): Sequence<Point> = asSequence().mapIndexed { y, row ->
    row.mapIndexed { x, item ->
        Point(x, y) to item
    }
}.flatten().filter {
    predicate(it.second)
}.map {
    it.first
}

operator fun <T> List<List<T>>.contains(point: Point): Boolean {
    return point.y in this.indices && point.x in this[0].indices
}