package aoc2015

import util.input
import kotlin.streams.toList

const val SECONDS = 2503

data class Reindeer(
        val name: String,
        val speed: Int,
        val speedTime: Int,
        val restTime: Int
)

private fun String.toReindeer(): Reindeer {
    val tokens = this.split(" ")
    return Reindeer(
            tokens[0],
            tokens[3].toInt(),
            tokens[6].toInt(),
            tokens[13].toInt()
    )
}

private fun Reindeer.distanceAfterSeconds(seconds: Int): Int {
    val sprintTime = speedTime + restTime
    return (speed * speedTime * (seconds / sprintTime)) +
            (speed * Math.min(speedTime, seconds % sprintTime))
}

private fun maxTimeSpentInTheLead(reindeers: List<Reindeer>): Int {
    return (1..SECONDS).fold(mapOf<String, Int>()) { currentPoints, elapsed ->
        val currentLeaders = reindeers.groupBy {
            it.distanceAfterSeconds(elapsed)
        }.maxBy {
            it.key
        }!!.value.map {
            it.name to (currentPoints.getOrDefault(it.name, 0) + 1)
        }.toMap()
        currentPoints + currentLeaders
    }.maxBy { it.value }!!.value
}

fun main() {
    val reindeers = input("inputs/2015/14.txt").use { lines ->
        lines.toList().map { it.toReindeer() }
    }

    println(reindeers.map { it.distanceAfterSeconds(SECONDS) }.max())

    println(maxTimeSpentInTheLead(reindeers))
}