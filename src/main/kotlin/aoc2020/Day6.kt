package aoc2020

import java.io.File

private object Day6 {

    fun countUnique(lines: List<String>): Int {
        var uniqueCounts = 0
        val groupCounts = mutableSetOf<Char>()

        for (line in lines) {
            if (line.isBlank()) {
                uniqueCounts += groupCounts.size
                groupCounts.clear()
            } else {
                groupCounts.addAll(line.toList())
            }
        }
        uniqueCounts += groupCounts.size
        return uniqueCounts
    }

    fun countIntersect(lines: List<String>): Int {
        var totalCounts = 0
        val groupCounts = mutableSetOf<Char>()
        var add = true

        for (line in lines) {
            if (line.isBlank()) {
                add = true
                totalCounts += groupCounts.size
                groupCounts.clear()
            } else if (add) {
                groupCounts.addAll(line.toSet())
                add = false
            } else {
                groupCounts.retainAll(line.toSet())
            }
        }
        totalCounts += groupCounts.size
        return totalCounts
    }
}

fun main() {
    val lines = File("inputs/2020/6.txt").readLines()
    println(Day6.countUnique(lines))
    println(Day6.countIntersect(lines))
}