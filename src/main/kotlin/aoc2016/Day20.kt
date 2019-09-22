package aoc2016

import util.input
import kotlin.math.max
import kotlin.streams.asSequence
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private typealias IpRange = Pair<Long, Long>

private object Day20 {

    fun smallestIp(): Long {
        val intervals = mergeIntervals(parseInput())
        return intervals.first().second + 1
    }

    fun numberOfAllowedIps(): Long {
        val intervals = mergeIntervals(parseInput())//.take(1).also { println(it) }
        var sum = 0L
        for (i in 1 until intervals.size) {
            sum += (intervals[i].first - (intervals[i - 1].second) - 1)
        }
        return sum
    }

    private fun mergeIntervals(input: List<IpRange>): List<IpRange> {
        val sorted = input.sortedWith(compareBy({ it.first }, { it.second }))
        val merged = mutableListOf<IpRange>()
        var (start, end) = sorted.first()
        for (next in sorted.drop(1)) {
            if (next.first > end + 1) {
                merged.add(start to end)
                start = next.first
            }
            end = max(end, next.second)
        }
        merged.add(start to end)
        return merged
    }

    private fun parseInput(): List<Pair<Long, Long>> {
        return input("inputs/2016/20.txt").use {
            it.asSequence().map { line ->
                line.split("-").let { it[0].toLong() to it[1].toLong() }
            }.toList()
        }
    }
}

@ExperimentalTime
fun main() {
    println( measureTimedValue { Day20.smallestIp() })
    println( measureTimedValue { Day20.numberOfAllowedIps() })
}