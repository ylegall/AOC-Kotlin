package aoc2021

import util.MutableCounter
import java.io.File
import kotlin.math.abs
import kotlin.math.max


fun main() {

    data class Segment(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int,
    ) {
        fun toRange(): List<Pair<Int, Int>> {
            val steps = max(abs(x2 - x1), abs(y2 - y1))
            val dx = if (x1 == x2) 0 else (x2 - x1) / abs(x2 - x1)
            val dy = if (y1 == y2) 0 else (y2 - y1) / abs(y2 - y1)
            return (0 .. steps).map { i -> (x1 + i * dx) to (y1 + i * dy) }
        }
    }

    fun part1(segments: List<Segment>): Int {
        val counts = MutableCounter<Pair<Int, Int>>()
        for (segment in segments) {
            if (segment.x1 == segment.x2 || segment.y1 == segment.y2) {
                segment.toRange().forEach { point ->
                    counts.increment(point)
                }
            }
        }
        return counts.values.count { it >= 2 }
    }

    fun part2(segments: List<Segment>): Int {
        val counts = MutableCounter<Pair<Int, Int>>()
        for (segment in segments) {
            val range = segment.toRange()
            range.forEach { point ->
                counts.increment(point)
            }
        }
        return counts.values.count { it >= 2 }
    }

    val input = File("inputs/2021/5.txt").useLines { lines ->
        lines.map { line ->
            val tokens = line.split(" -> ", ",").map { it.toInt() }
            Segment(tokens[0], tokens[1], tokens[2], tokens[3])
        }.toList()
    }

    println(part1(input))
    println(part2(input))
}