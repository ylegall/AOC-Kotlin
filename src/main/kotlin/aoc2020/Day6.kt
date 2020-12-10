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
        var groupCounts = emptySet<Char>()
        for (line in lines) {
            if (line.isBlank()) {
                totalCounts += groupCounts.size
                groupCounts = emptySet()
            } else {
                groupCounts = if (groupCounts.isEmpty()) {
                    line.toSet()
                } else {
                    groupCounts.intersect(line.toSet())
                }
            }
        }
        return totalCounts
    }

}

fun main() {
    val lines = File("inputs/2020/6.txt").readLines()
    println(Day6.countUnique(lines))
    println(Day6.countIntersect(lines))
}