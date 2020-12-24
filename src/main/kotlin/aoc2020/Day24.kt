package aoc2020

import util.counts
import java.io.File

private object Day24 {

    private val validDirs = setOf("nw", "ne", "e", "se", "sw", "w")

    private fun split(directions: String): List<String> {
        val sb = StringBuilder()
        val dirs = mutableListOf<String>()
        for (c in directions) {
            sb.append(c)
            val str = sb.toString()
            if (str.length == 2 || str in validDirs) {
                sb.clear()
                dirs.add(str)
            }
        }
        return dirs
    }

    fun getCoord(line: String): Pair<Int, Int> {
        val splitDirections = split(line)
        val counts = counts(splitDirections)
        val nw = counts["nw"] - counts["se"]
        val ne = counts["ne"] - counts["sw"]
        val e = counts["e"] - counts["w"]
        val x = e + ne
        val y = nw + ne
        return x.toInt() to y.toInt()
    }

    fun countFlipped(lines: List<String>): Set<Pair<Int, Int>> {
        val flipped = mutableSetOf<Pair<Int, Int>>()
        for (directions in lines) {
            val coord = getCoord(directions)
            if (coord in flipped) {
                flipped.remove(coord)
            } else {
                flipped.add(coord)
            }
        }
        return flipped
    }

    fun Pair<Int, Int>.neighbors() = listOf(
            first - 1 to second + 0,
            first + 0 to second + 1,
            first + 1 to second + 1,
            first + 1 to second + 0,
            first + 0 to second - 1,
            first - 1 to second - 1,
    )

    fun countFlipppedAfter100Days(initialTiles: Set<Pair<Int, Int>>, days: Int = 100): Int {
        var tiles = initialTiles
        for (day in 1 .. days) {
            val minX = tiles.minByOrNull { it.first }!!.first - 1
            val maxX = tiles.maxByOrNull { it.first }!!.first + 1
            val minY = tiles.minByOrNull { it.second }!!.second - 1
            val maxY = tiles.maxByOrNull { it.second }!!.second + 1

            val newTiles = mutableSetOf<Pair<Int, Int>>()
            for (x in minX .. maxX) {
                for (y in minY .. maxY) {
                    val tile = x to y
                    val activeNeighbors = tile.neighbors().count { it in tiles }
                    if (tile in tiles) {
                        if (activeNeighbors in 1..2) {
                            newTiles.add(tile)
                        }
                    } else {
                        if (activeNeighbors == 2) {
                            newTiles.add(tile)
                        }
                    }
                }
            }
            tiles = newTiles
            //println("Day $day: ${tiles.size}")
        }
        return tiles.size
    }

}

fun main() {
    val input = File("inputs/2020/24.txt").readLines()
    val initialTiles = Day24.countFlipped(input)
    println(initialTiles.size)

    println(Day24.countFlipppedAfter100Days(initialTiles, 100))
}
