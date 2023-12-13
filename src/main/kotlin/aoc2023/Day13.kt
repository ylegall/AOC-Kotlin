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

    fun isReflectionAtCol(input: List<String>, col: Int): Boolean {
        require(col > 0 && col < input[0].length) { "row out of bounds" }
        val (left, right) = input.partitionAtCol(col)
        return left.zip(right).all { it.first == it.second }
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

    fun isReflectionAtRow(input: List<String>, row: Int): Boolean {
        val (bottom, top) = input.partitionAtRow(row)
        return bottom == top
    }

    fun mismatches(left: List<String>, right: List<String>): Int {
        return left.zip(right).sumOf { (s1, s2) ->
            s1.zip(s2).count { (a, b) -> a != b }
        }
    }

    fun part1() {
        val result = input.map { region ->
            val vertical = (1 until region[0].length).firstOrNull { col -> isReflectionAtCol(region, col) } ?: 0
            val horizontal = (1 until region.size).firstOrNull { row -> isReflectionAtRow(region, row) } ?: 0
            Pair(vertical, horizontal)
        }.sumOf { it.first + 100 * it.second }
        println(result)
    }

    fun part2() {
        val result = input.sumOf { region ->
            val singleErrorRows = (1 until region.size).asSequence().mapNotNull { row ->
                val (bottom, top) = region.partitionAtRow(row)
                val mismatchCount = mismatches(bottom, top)
                row.takeIf { mismatchCount == 1 }
            }.toList()

            val singleErrorCols = (1 until region[0].length).asSequence().mapNotNull { col ->
                val (left, right) = region.partitionAtCol(col)
                val mismatchCount = mismatches(left, right)
                col.takeIf { mismatchCount == 1 }
            }.toList()

            if (singleErrorRows.isNotEmpty()) {
                100 * singleErrorRows.first()
            } else {
                singleErrorCols.first()
            }
        }
        println(result)
    }

    part1()
    part2()
}