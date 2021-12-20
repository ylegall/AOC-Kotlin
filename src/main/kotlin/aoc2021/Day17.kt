package aoc2021

import kotlin.math.abs
import kotlin.math.max


fun main() {

//    val xBounds = 20 .. 30
//    val yBounds = -10 .. -5
    val xBounds = 102 .. 157
    val yBounds = -146 .. -90

    fun isInBouds(x: Int, y: Int) = x in xBounds && y in yBounds

    fun minXVelocity(): Int {
        for (i in 1 until xBounds.first) {
            val sum = i * (i + 1) / 2
            if (sum >= xBounds.first) {
                return i
            }
        }
        return -1
    }

    fun maxXVelocity() = xBounds.last
    fun minYVelocity() = yBounds.first
    fun maxYVelocity() = -minYVelocity()

    fun simulate(vx: Int, vy: Int): Boolean {
        var px = 0
        var py = 0
        var vx = vx
        var vy = vy

        while (true) {
            px += vx
            py += vy
            if (isInBouds(px, py)) {
                return true
            }
            vx = when {
                vx > 0 -> vx - 1
                vx < 0 -> vx + 1
                else -> 0
            }
            vy -= 1

            if (py < yBounds.start) return false
            if (px > xBounds.endInclusive) return false
        }
    }


    fun part1(): Int {
        var dy = abs(yBounds.first)
        var prev = yBounds.first
        var y = 0
        var maxY = prev
        while (prev <= y) {
            maxY = max(y, maxY)
            dy -= 1
            prev = y
            y += dy
        }
        return maxY
    }

    fun part2(): Long {
        val minVx = minXVelocity()
        val maxVx = maxXVelocity()
        val minVy = minYVelocity()
        val maxVy = maxYVelocity()

        var count = 0L
        for (vx in minVx .. maxVx) {
            for (vy in minVy .. maxVy) {
                if (simulate(vx, vy)) {
                    println("$vx, $vy")
                    count++
                }
            }
        }
        return count
    }

    println(part1())
    println(part2())
}