package aoc2017

import java.io.File
import kotlin.math.max
import kotlin.math.min

// part 1
private fun minMaxDiffSum(rows: List<List<Int>>): Int {
    return rows.map { it.maxOrNull()!! - it.minOrNull()!! }.sum()
}

// part 2
private fun sumDivisors(rows: List<List<Int>>): Int {
    var sum = 0
    outer@ for (row in rows) {
        for (i in 0 until row.size - 1) {
            for (j in i + 1 until row.size) {
                val max = max(row[i], row[j])
                val min = min(row[i], row[j])
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
    val rows = File("inputs/2017/2.txt").readLines().map { line ->
        line.split('\t').map { it.trim().toInt() }
    }
    println(minMaxDiffSum(rows))
    println(sumDivisors(rows))
}
