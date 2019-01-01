package aoc2017

const val TARGET = 289326

private fun ringNumber(n: Int): Int {
    val nn = Math.sqrt(n - 1.0).toInt()
    return nn - (nn / 2)
}

private fun stepsToCenter(n: Int): Int {
    val ring = ringNumber(n)
    val length = (ring * 2) + 1
    val corner = length * length
    val midPoints = (0 .. 3).map { corner - ring - (it * (length - 1)) }
    val stepsToMiddle = midPoints.map { Math.abs(n - it) }.min()!!
    return ring + stepsToMiddle
}


fun main() {
    /// part 1:
    println(stepsToCenter(TARGET))
}
