package aoc2018

import util.max
import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs

private object Day23 {

    data class Point3(val x: Long, val y: Long, val z: Long) {
        fun distanceTo(other: Point3) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    data class Bot(val pos: Point3, val r: Long) {
        fun isPointInRange(point: Point3) = pos.distanceTo(point) <= r
    }

    private data class Box(val pos: Point3, val len: Long) {
        fun isBotInRange(bot: Bot): Boolean {
            val closestPointInBox = Point3(
                    pos.x + (bot.pos.x - pos.x).coerceIn(0, len),
                    pos.y + (bot.pos.y - pos.y).coerceIn(0, len),
                    pos.z + (bot.pos.z - pos.z).coerceIn(0, len)
            )
            return bot.isPointInRange(closestPointInBox)
        }
    }

    private data class SearchSpace(val box: Box, val botsInRange: Int) {

        fun split(bots: List<Bot>): List<SearchSpace> {
            val newlen = box.len / 2L
            return (0 .. 1).flatMap { i ->
                (0 .. 1).flatMap { j ->
                    (0 .. 1).map { k ->
                        val newPoint = Point3(
                                box.pos.x + i * newlen,
                                box.pos.y + j * newlen,
                                box.pos.z + k * newlen
                        )
                        val newBox = Box(newPoint, newlen)
                        SearchSpace(newBox, bots.count { newBox.isBotInRange(it) })
                    }
                }
            }
        }
    }

    fun parseInput(): List<Bot> {
        return File("inputs/2018/23.txt").useLines { lines ->
            lines.map { line ->
                val tokens = line.split(Regex("""[^-0-9]+""")).filter { it.isNotBlank() }.map { it.toLong() }
                Bot(Point3(tokens[0], tokens[1], tokens[2]), tokens[3])
            }.toList()
        }
    }

    fun botsInRangeOfStrongest(bots: List<Bot>): Int {
        val strongestBot = bots.maxByOrNull { it.r }!!
        return bots.filter { strongestBot.isPointInRange(it.pos) }.size
    }

    fun closestPointToAllBots(bots: List<Bot>) {
        val maxDimension = bots.map { max(abs(it.pos.x), abs(it.pos.y), abs(it.pos.z))!! }.maxOrNull()!!
        val initialSize = Integer.highestOneBit(maxDimension.toInt()) * 2L
        val origin = Point3(0, 0, 0)
        val initialBox = Box(Point3(-initialSize, -initialSize, -initialSize), initialSize * 2L)

        val queue = PriorityQueue<SearchSpace>(compareBy({ -it.botsInRange }, { it.box.pos.distanceTo(origin) }))
        queue.add(SearchSpace(initialBox, 0))
        while (queue.isNotEmpty()) {
            val bestRegion = queue.poll()
            println(bestRegion)
            if (bestRegion.box.len == 0L) {
                println("found best point: $bestRegion")
                println(bestRegion.box.pos.distanceTo(origin))
                return
            }
            val nextRegions = bestRegion.split(bots)
            queue.addAll(nextRegions)
        }
    }
}

fun main() {
    val bots = Day23.parseInput()
    println(Day23.botsInRangeOfStrongest(bots))
    Day23.closestPointToAllBots(bots)
}