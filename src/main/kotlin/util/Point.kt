package util

data class Point(val x: Int, val y: Int)

fun Point.mDist(x: Int, y: Int): Int {
    return Math.abs(this.x - x) + Math.abs(this.y - y)
}