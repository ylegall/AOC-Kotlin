package aoc2017

import util.input
import kotlin.streams.toList

// part 1
private fun minMaxDiffSum(rows: List<List<Int>>): Int {
    return rows.map { it.max()!! - it.min()!! }.sum()
}

// part 2
private fun sumDivisors(rows: List<List<Int>>): Int {
    var sum = 0
    outer@ for (row in rows) {
        for (i in 0 until row.size - 1) {
            for (j in i + 1 until row.size) {
                val max = Math.max(row[i], row[j])
                val min = Math.min(row[i], row[j])
                if (max % min == 0) {
                    sum += (max / min)
                    continue@outer
                }
            }
        }
    }
    return sum
}

fun main() {
    val rows = input("inputs/2017/2.txt").use { lines ->
        lines.map { line ->
            line.split('\t').map { it.trim().toInt() }
        }.toList()
    }
    println(minMaxDiffSum(rows))
    println(sumDivisors(rows))
}