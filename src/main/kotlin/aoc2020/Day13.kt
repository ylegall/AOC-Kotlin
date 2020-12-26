package aoc2020

import java.io.File
import kotlin.math.ceil


private fun inverseMod(x: Long, mod: Long): Long {
    for (i in 0 until mod) {
        val n = (x * i) % mod
        if (n == 1L) {
            return i
        }
    }
    return -1L
}

private fun waitTime(targetTime: Long, busId: Long): Long {
    val loops = ceil(targetTime.toDouble() / busId).toInt()
    val soonestArrival = loops * busId
    return soonestArrival - targetTime
}

private fun earliestBus(targetTime: Long, schedule: List<Long>): Long {
    return schedule.map { it to waitTime(targetTime, it) }
            .minByOrNull { it.second }
            ?.let { it.first * it.second }!!
}

// chinese remainder theorem
private fun findMagicTimestamp(schedule: List<Pair<Long, Long>>): Long {
    val product = schedule.fold(1L) { a, item -> a * item.first }

    var sum = 0L
    for ((id, offset) in schedule) {
        val n = product / id
        sum += ((id - offset) * n * inverseMod(n, id))
    }
    return sum % product
}

fun main() {
    val input = File("inputs/2020/13.txt").readLines()

    val earliestTime = input[0].toLong()
    val schedules = input[1].split(",")
            .filter { it != "x" }
            .map { it.toLong() }

    println(earliestBus(earliestTime, schedules))

    val schedulesWithIndex = input[1].split(",").mapIndexed { idx, id -> id to idx }
            .filter { it.first != "x" }
            .map { it.first.toLong() to it.second.toLong() }

    println(findMagicTimestamp(schedulesWithIndex))
}