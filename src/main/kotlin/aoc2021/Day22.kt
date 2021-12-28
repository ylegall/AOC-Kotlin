package aoc2021

import java.io.File

fun main() {

    fun IntRange.overlaps(other: IntRange): Boolean {
        return !(endInclusive < other.first || first > other.last)
    }

    operator fun IntRange.contains(other: IntRange): Boolean {
        return other.first in this && other.last in this
    }

    data class Region(
        val xRange: IntRange,
        val yRange: IntRange,
        val zRange: IntRange,
    ) {
        fun overlaps(other: Region): Boolean {
            if (!xRange.overlaps(other.xRange)) return false
            if (!yRange.overlaps(other.yRange)) return false
            if (!zRange.overlaps(other.zRange)) return false
            return true
        }

        fun volume(): Long {
            return (xRange.last - xRange.first + 1L) *
                    (yRange.last - yRange.first + 1L) *
                    (zRange.last - zRange.first + 1L)
        }

        operator fun contains(other: Region): Boolean {
            return other.xRange in xRange &&
                    other.yRange in yRange &&
                    other.zRange in zRange
        }
    }

    data class Action(
        val on: Boolean,
        val region: Region
    )

    fun splitInterval(r1: IntRange, r2: IntRange): List<IntRange> {
        val intervals = when {
            r1.first <= r2.first -> {
                when {
                    r1.last < r2.last -> listOf(r1.first until r2.first, r2.first .. r1.last)
                    else              -> listOf(r1.first until r2.first, r2.first .. r2.last, r2.last + 1 .. r1.last)
                }
            }
            else -> {
                when {
                    r1.last < r2.last -> listOf(r1.first .. r1.last)
                    else              -> listOf(r1.first .. r2.last, r2.last + 1 .. r1.last)
                }
            }
        }
        return intervals.filter { it.first <= it.last }
    }

    fun split(first: Region, second: Region): List<Region> {
        val xIntervals = splitInterval(first.xRange, second.xRange)
        val yIntervals = splitInterval(first.yRange, second.yRange)
        val zIntervals = splitInterval(first.zRange, second.zRange)
        val newRegions = mutableListOf<Region>()
        for (xInterval in xIntervals) {
            for (yInterval in yIntervals) {
                for (zInterval in zIntervals) {
                    newRegions.add(Region(xInterval, yInterval, zInterval))
                }
            }
        }
        val filtered = newRegions.filter { it !in second }
        return filtered
    }

    fun split(action1: Action, action2: Action): List<Action> {
        return if (action1.region.overlaps(action2.region)) {
            split(action1.region, action2.region).map { Action(action1.on, it) }
        } else {
            listOf(action1)
        }
    }

    fun part2(actions: List<Action>): Long {
        var finalActions = actions.take(1)
        for (newAction in actions.drop(1)) {
            finalActions = finalActions.flatMap { action1 ->
                split(action1, newAction)
            } + newAction
        }
        return finalActions.filter { it.on }.sumOf { it.region.volume() }
    }

    fun parseAction(line: String): Action {
        val tokens = line.split(" ", "=", "..", ",")
        val region = Region(
            tokens[2].toInt() .. tokens[3].toInt(),
            tokens[5].toInt() .. tokens[6].toInt(),
            tokens[8].toInt() .. tokens[9].toInt(),
        )
        return Action(tokens[0] == "on", region)
    }

    val actions = File("inputs/2021/input.txt").useLines { lines ->
        lines.map { parseAction(it) }.toList()
    }

    val smallBounds = Region(-50..50, -50..50, -50..50)
    println(part2(actions.filter { it.region in smallBounds }))
    println(part2(actions))
}
