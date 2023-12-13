package aoc2023

import util.split
import java.io.File

fun main() {

    val input = File("input.txt").readLines().split { it == "" }

    fun String.partitionAtCol(col: Int): Pair<String, String> {
        val left = take(col)
        val right = slice(col until length)
        return if (left.length > right.length) {
            left.drop(left.length - right.length) to right.reversed()
        } else {
            left to right.slice(left.indices).reversed()
        }
    }

    fun List<String>.partitionAtCol(col: Int): Pair<List<String>, List<String>> {
        return map { row ->
            val (left, right) = row.partitionAtCol(col)
            listOf(left) to listOf(right)
        }.reduce { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }
    }

    fun List<String>.partitionAtRow(row: Int): Pair<List<String>, List<String>> {
        require(row in 1 until size) { "row out of bounds" }
        val bottom = take(row)
        val top = slice(row until size)
        return if (bottom.size > top.size) {
            bottom.drop(bottom.size - top.size) to top.reversed()
        } else {
            bottom to top.take(bottom.size).reversed()
        }
    }

    fun mismatches(left: List<String>, right: List<String>): Int {
        return left.zip(right).sumOf { (s1, s2) ->
            s1.zip(s2).count { (a, b) -> a != b }
        }
    }

    fun findReflections(input: List<List<String>>, mismatches: Int): Int {
        return input.sumOf { region ->
            val rowReflections = (1 until region.size).asSequence().mapNotNull { row ->
                val (bottom, top) = region.partitionAtRow(row)
                row.takeIf { mismatches(bottom, top) == mismatches }
            }.firstOrNull() ?: 0

            val colReflections = (1 until region[0].length).asSequence().mapNotNull { col ->
                val (left, right) = region.partitionAtCol(col)
                col.takeIf { mismatches(left, right) == mismatches }
            }.firstOrNull() ?: 0

            colReflections + 100 * rowReflections
        }
    }

    fun part1() {
        println(findReflections(input, 0))
    }

    fun part2() {
        println(findReflections(input, 1))
    }

    part1()
    part2()
}