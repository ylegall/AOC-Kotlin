package aoc2023

import util.split
import java.io.File

private object Day5 {

    data class RangeMap(
        val srcRange: LongRange,
        val offset: Long
    )

    data class Input(
        val seeds: List<Long>,
        val rangeMaps: List<List<RangeMap>>
    )

    fun parseInput(): Input {
        val lines = File("input.txt").readLines()
        val seeds = lines.first().split(" ").drop(1).map { it.trim().toLong() }
        val maps = lines.drop(2).split { it.isBlank() }.map { section ->
            val ranges = section.drop(1).map { line ->
                val (dstStart, srcStart, length) = line.split(" ").map { it.trim().toLong() }
                RangeMap(LongRange(srcStart, srcStart + length - 1), dstStart - srcStart)
            }
            ranges
        }
        return Input(seeds, maps)
    }

    fun LongRange.offset(offset: Long) = LongRange(first + offset, last + offset)

    fun convertToLocation(n: Long, input: Input): Long {
        var value = n
        for (map in input.rangeMaps) {
            for (range in map) {
                if (value in range.srcRange) {
                    value += range.offset
                    break
                }
            }
        }
        return value
    }

    fun convertToLocationRecursive(range: LongRange, input: Input, index: Int=0): List<LongRange> {
        return if (index >= input.rangeMaps.size) {
            listOf(range)
        } else {
            val map = input.rangeMaps[index]
            val firstRange = map.firstOrNull { range.first in it.srcRange }
            if (firstRange != null) {
                if (range.last in firstRange.srcRange) {
                    val newRange = range.offset(firstRange.offset)
                    convertToLocationRecursive(newRange, input, index + 1)
                } else {
                    val prefix = LongRange(range.first, firstRange.srcRange.last).offset(firstRange.offset)
                    val suffix = LongRange(firstRange.srcRange.last + 1, range.last)
                    convertToLocationRecursive(prefix, input, index + 1) +
                            convertToLocationRecursive(suffix, input, index + 1)
                }
            } else {
                val lastRange = map.firstOrNull { range.last in it.srcRange }
                if (lastRange != null) {
                    val prefix = LongRange(range.first, lastRange.srcRange.first-1)
                    val suffix = LongRange(lastRange.srcRange.first, range.last).offset(lastRange.offset)
                    convertToLocationRecursive(prefix, input, index + 1) +
                            convertToLocationRecursive(suffix, input, index + 1)
                } else {
                    convertToLocationRecursive(range, input, index + 1)
                }
            }
        }
    }

    fun part1(input: Input) {
        println(input.seeds.minOf { convertToLocation(it, input) })
    }

    fun part2(input: Input) {
        val seedRanges = input.seeds.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }
        println(seedRanges.flatMap { convertToLocationRecursive(it, input) }.minOf { it.first })
    }
}

fun main() {
    val input = Day5.parseInput()
    Day5.part1(input)
    Day5.part2(input)
}