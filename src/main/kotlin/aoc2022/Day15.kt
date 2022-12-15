package aoc2022

import util.Point
import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val pattern = """x=(-?\d+), y=(-?\d+)""".toRegex()
    val input = File("input.txt").useLines { lines ->
        lines.map { line ->
            pattern.findAll(line).map { match ->
                val (x, y) = match.destructured
                Point(x.toInt(), y.toInt())
            }.toList()
        }.toList()
    }

    val sensorRanges = input.map { (sensor, beacon) -> sensor to sensor.mDist(beacon) }
    val beaconsByRow = input.map { (_, beacon) -> beacon }.groupBy { it.y }.mapValues { (_, beacons) -> beacons.toSet() }

    fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
        val sorted = ranges.sortedWith(compareBy({ it.first }, { it.last }))
        return sorted.drop(1).fold(listOf(sorted.first())) { list, nextRange ->
            val currentRange = list.last().let { LongRange(it.first, it.last + 1) }
            if (nextRange.first in currentRange) {
                list.dropLast(1) + listOf(LongRange(currentRange.first, max(currentRange.last-1, nextRange.last)))
            } else {
                list + listOf(nextRange)
            }
        }
    }

    fun coveredRangeForRow(rowOfInterest: Int): List<LongRange> {
        val xRanges = sensorRanges.mapNotNull { (sensor, range) ->
            val remaining = range - abs(sensor.y - rowOfInterest).toLong()
            if (remaining >= 0) {
                LongRange(sensor.x - remaining, sensor.x + remaining)
            } else {
                null
            }
        }
        return mergeRanges(xRanges)
    }

    fun part1() {
        // val rowOfInterest = 10
        val rowOfInterest = 2000000

        val rowRanges = coveredRangeForRow(rowOfInterest)
        val beaconsOnRow = beaconsByRow[rowOfInterest]?.size ?: 0
        val seenCount = mergeRanges(rowRanges)
            .sumOf { it.last - it.first + 1 }
            .let { it - beaconsOnRow }
        println(seenCount)
    }

    fun part2() {
        // val maxCoord = 20
        val maxCoord = 4000000
        val tuningFrequency = (0..maxCoord)
            .map { row -> row to coveredRangeForRow(row) }
            .first { it.second.size > 1 }
            .let { (row, ranges) ->
                val x = ranges.first().last + 1
                x * 4000000 + row
            }
        println(tuningFrequency)
    }

    part1()
    part2()
    
}
