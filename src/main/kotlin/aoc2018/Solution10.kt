package aoc2018

import input

data class Vec(var x: Int, var y: Int)

const val LIMIT = 20000

private fun findMessage(positions: List<Vec>, velocities: List<Vec>) {

    var minH = Int.MAX_VALUE
    var minW = Int.MAX_VALUE

    for (t in 0 ..LIMIT) {

        var minX = positions[0].x
        var minY = positions[0].y
        var maxX = positions[0].x
        var maxY = positions[0].y

        for (i in 0 until positions.size) {
            positions[i].x += velocities[i].x
            positions[i].y += velocities[i].y

            if (positions[i].x < minX) minX = positions[i].x
            else if (positions[i].x > maxX) maxX = positions[i].x

            if (positions[i].y < minY) minY = positions[i].y
            else if (positions[i].y > maxY) maxY = positions[i].y
        }

        val w = maxX - minX + 1
        val h = maxY - minY + 1

        if (w < minW) minW = w
        if (h < minH) minH = h

        println("$t: area: ${w * h}, minW: $minW, minH: $minH")
    }

    /*
    val minX = positions.minBy { it.x }!!.x
    val minY = positions.minBy { it.y }!!.y
    val maxX = positions.maxBy { it.x }!!.x
    val maxY = positions.maxBy { it.y }!!.y

    val w = maxX - minX + 1
    val h = maxY - minY + 1

    val grid = Array(h) { Array(w) { ' ' } }

    for (p in positions) {
        grid[p.y - minY][p.x - minX] = '#'
    }

    for (row in 0 until h) {
        for (col in 0 until w) {
            print(grid[row][col])
        }
        println()
    }
    println()
    */
}

fun main() {
    val positions = arrayListOf<Vec>()
    val velocities = arrayListOf<Vec>()

    input("inputs/2018/10.txt").use { lines ->
        for (line in lines) {
            val tokens = line.split(',', '<', '>').map { it.trim() }
            positions.add(Vec(tokens[1].toInt(), tokens[2].toInt()))
            velocities.add(Vec(tokens[4].toInt(), tokens[5].toInt()))
        }
    }

    findMessage(positions, velocities)
}