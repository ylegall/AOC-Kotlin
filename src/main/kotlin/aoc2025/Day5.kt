package aoc2025

import aoc2025.Day5.binarySearch
import aoc2025.Day5.sortAndMergeIntervals
import util.split
import java.io.File
import kotlin.math.max
import kotlin.math.min

private object Day5 {

    class Input(
        val ranges: List<LongRange>,
        val ids: List<Long>
    )

    fun parseInput(): Input {
        val parts = File("input.txt").readLines().split { it.isEmpty() }

        val ranges = parts[0].map { line ->
            line.split('-').let { LongRange(it[0].toLong(), it[1].toLong()) }
        }
        val ids = parts[1].map { it.toLong() }
        return Input(ranges, ids)
    }

    fun mergeRanges(range1: LongRange, range2: LongRange): LongRange? {
        return if (range1.first > range2.last || range1.last < range2.first) {
            null
        } else {
            LongRange(
                min(range1.first, range2.first),
                max(range1.last, range2.last)
            )
        }
    }

    fun sortAndMergeIntervals(input: Input): List<LongRange> {
        val mergedResults = mutableListOf<LongRange>()
        val sortedRanges = input.ranges.sortedBy { it.first }
        var currentRange = sortedRanges.first()
        for (i in 1 until sortedRanges.size) {
            val merged = mergeRanges(currentRange, sortedRanges[i])
            if (merged != null) {
                currentRange = merged
            } else {
                mergedResults.add(currentRange)
                currentRange = sortedRanges[i]
            }
        }
        mergedResults.add(currentRange)
        return mergedResults
    }

    fun List<LongRange>.binarySearch(id: Long): Int {
        var low = 0
        var high = this.size-1
        while (low <= high) {
            val mid = low + (high-low)/2
            val interval = this[mid]
            if (id < interval.first) {
                high = mid-1
            } else if (id > interval.last) {
                low = mid+1
            } else {
                return mid
            }
        }
        return -1
    }
}

fun main() {
    val input = Day5.parseInput()
    val sortedAndMergedIntervals = sortAndMergeIntervals(input)

    // part 1
    val numFreshIds = input.ids.count { id -> sortedAndMergedIntervals.binarySearch(id) >= 0 }
    println(numFreshIds)

    // part 2
    val totalFreshIds = sortedAndMergedIntervals.sumOf { it.last - it.first + 1 }
    println(totalFreshIds)
}
