package aoc2018

import input
import java.util.stream.Stream

private fun MatchResult.group(idx: Int): Int {
    return this.groups[idx]?.value?.toInt() ?: throw Exception("error selecting `aoc2018`.group $idx")
}

private fun countOverlappingRectangles(stream: Stream<String>): Int {
    val pattern = Regex("""@ (\d+),(\d+): (\d+)x(\d+)""")
    val points = hashMapOf<Pair<Int,Int>, Int>()
    for (line in stream) {
        val match = pattern.find(line) ?: throw Exception("error parsing line: $line")
        val x = match.group(1)
        val y = match.group(2)
        val w = match.group(3)
        val h = match.group(4)
        for (i in x until x + w) {
            for (j in y until y + h) {
                val point = Pair(i,j)
                points[point] = points.getOrDefault(point, 0) + 1
            }
        }
    }
    return points.values.filter { it >= 2 }.size
}

private fun findNonOverlappingRectangle(stream: Stream<String>): Int {
    var maxId = 0
    val overlaps = hashSetOf<Int>()
    val pattern = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")
    val points = hashMapOf<Pair<Int,Int>, Int>()
    for (line in stream) {
        val match = pattern.find(line) ?: throw Exception("error parsing line: $line")
        val id = match.group(1)
        val x = match.group(2)
        val y = match.group(3)
        val w = match.group(4)
        val h = match.group(5)
        for (i in x until x + w) {
            for (j in y until y + h) {
                val point = Pair(i,j)
                val lastId = points.getOrDefault(point, id)
                if (lastId != id) {
                    overlaps.add(id)
                    overlaps.add(lastId)
                }
                points[point] = id
            }
        }
        if (id >= maxId) maxId = id
    }
    return (1..maxId).first { it !in overlaps}
}

fun main() {
    input("inputs/2018/3.txt").use { stream ->
        println(countOverlappingRectangles(stream))
    }
    input("inputs/2018/3.txt").use { stream ->
        println(findNonOverlappingRectangle(stream))
    }
}