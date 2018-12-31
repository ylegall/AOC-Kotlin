package util

data class Point(
        val x: Int,
        val y: Int
): Comparable<Point> {
    override fun compareTo(other: Point) = pointComparator.compare(this, other)
}

val pointComparator = compareBy<Point>({ it.y }, { it.x })

fun Point.mDist(point: Point) = mDist(point.x, point.y)

fun Point.mDist(x: Int, y: Int): Int {
    return Math.abs(this.x - x) + Math.abs(this.y - y)
}
