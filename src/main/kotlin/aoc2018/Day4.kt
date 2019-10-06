package aoc2018

import util.input
import java.util.stream.Stream
import kotlin.streams.toList

private sealed class Event
{
    data class WakeUp(val min: Int): Event()
    data class Sleep(val min: Int): Event()
    data class BeginShift(val id: Int, val min: Int): Event()
}

private fun parseLine(line: String): Event {
    val pattern = Regex(""":(\d\d)] (falls asleep|wakes up|Guard #(\d+) begins shift)""")
    val match = pattern.find(line) ?: throw Exception("error parsing line: $line")
    val min = match.groups[1]?.value?.toInt()!!
    val event = match.groups[2]?.value!!
    return when (event) {
        "falls asleep" -> Event.Sleep(min)
        "wakes up" -> Event.WakeUp(min)
        else -> Event.BeginShift(match.groups[3]?.value?.toInt()!!, min)
    }
}

private fun findMaxSleepMinute(lines: Stream<String>) {
    val guardSleepTimes = hashMapOf<Int, Int>()
    val sleepMinutes = hashMapOf<Pair<Int, Int>, Int>()
    var guardId = -1
    var start = -1
    lines.toList().sorted().map { parseLine(it) }.forEach { event ->
        when (event) {
            is Event.BeginShift -> guardId = event.id
            is Event.Sleep -> start = event.min
            is Event.WakeUp -> {
                val sleepDuration = event.min - start
                guardSleepTimes[guardId] = guardSleepTimes.getOrDefault(guardId, 0) + sleepDuration
                (start until event.min).forEach { min ->
                    val key = Pair(guardId, min)
                    sleepMinutes[key] = sleepMinutes.getOrDefault(key, 0) + 1
                }
                start = -1
            }
        }
    }
    val sleepiest = guardSleepTimes.entries.maxBy { it.value }?.key!!
    val sleepiestMinute = sleepMinutes.entries.filter { it.key.first == sleepiest }.maxBy { it.value }!!
    val totalSeepiestMinute = sleepMinutes.entries.maxBy { it.value }!!
    println("answer part a:" + (sleepiest * sleepiestMinute.key.second))
    println("answer part b:" + (totalSeepiestMinute.key.first * totalSeepiestMinute.key.second))
}

fun main() {
    input("inputs/2018/4.txt").use { stream ->
        findMaxSleepMinute(stream)
    }
}