import kotlin.streams.asSequence

data class Point(val x: Int, val y: Int)

fun Point.mDist(x: Int, y: Int): Int {
    return Math.abs(this.x - x) + Math.abs(this.y - y)
}

inline fun <T, R: Comparable<R>> Iterable<T>.minByOrNull(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElement: T? = iterator.next()
    var minValue = selector(minElement!!)
    while (iterator.hasNext()) {
        val e = iterator.next()
        val v = selector(e)
        if (v < minValue) {
            minElement = e
            minValue = v
        } else if (v == minValue && e != minElement) {
            minElement = null
        }
    }
    return minElement
}

fun largestContainedRegion(points: Map<Int, Point>) {
    val areas = hashMapOf<Int, Int>()
    val edgePoints = hashSetOf<Int>()

    val maxX = points.values.maxBy { it.x }?.x!!
    val maxY = points.values.maxBy { it.y }?.y!!
    val minX = points.values.minBy { it.x }?.x!!
    val minY = points.values.minBy { it.y }?.y!!

    (minX .. maxX).forEach { col ->
        points.entries.minByOrNull { it.value.mDist(col, minY) }?.let { edgePoints.add(it.key) }
        points.entries.minByOrNull { it.value.mDist(col, maxY) }?.let { edgePoints.add(it.key) }
    }
    (minY .. maxY).forEach { row ->
        points.entries.minByOrNull { it.value.mDist(minX, row) }?.let { edgePoints.add(it.key) }
        points.entries.minByOrNull { it.value.mDist(maxX, row) }?.let { edgePoints.add(it.key) }
    }

    for (row in minY .. maxY) {
        for (col in minX .. maxX) {
            points.entries.minByOrNull {
                it.value.mDist(col, row)
            }?.takeIf {
                it.key !in edgePoints
            }?.key?.let {
                areas[it] = areas.getOrDefault(it, 0) + 1
            }
        }
    }

    println("largest area: " + areas.maxBy { it.value }?.value)
}

fun areaOfRegionWithin10000(points: Map<Int, Point>) {
    val maxX = points.values.maxBy { it.x }?.x!!
    val maxY = points.values.maxBy { it.y }?.y!!
    val minX = points.values.minBy { it.x }?.x!!
    val minY = points.values.minBy { it.y }?.y!!

    var area = 0
    for (row in minY .. maxY) {
        for (col in minX..maxX) {
            val totalDist = points.values.map { it.mDist(col, row) }.sum()
            if (totalDist < 10000) area += 1
        }
    }
    println("area: $area")
}

fun main() {
    input("inputs/input-6.txt").use { lines ->

        val points = lines.asSequence().mapIndexed { idx, line ->
            val tokens = line.split(",").map { it.trim().toInt() }
            idx to Point(tokens[0], tokens[1])
        }.toMap()

        largestContainedRegion(points)
        areaOfRegionWithin10000(points)
    }
}