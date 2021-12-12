package aoc2021

import util.Counter
import util.arrayDequeOf
import java.io.File

fun main() {

    class State(
        val current: String = "start",
        val counts: Counter<String> = Counter()
    )

    fun String.isSmall() = this.all { it.isLowerCase() }

    fun part1(graph: Map<String, List<String>>): Int {
        var totalPaths = 0
        val q = arrayDequeOf(State("start"))
        while (q.isNotEmpty()) {
            val state = q.poll()
            val counts = state.counts
            val current = state.current
            if (current == "end") {
                totalPaths++
            } else {
                val newCounts = counts.copy()
                if (current.isSmall()) {
                    newCounts.increment(current)
                }
                val neighbors = graph[current]?.filter { it !in newCounts } ?: emptyList()
                for (next in neighbors) {
                    q.add(State(next, newCounts))
                }
            }
        }
        return totalPaths
    }

    fun part2(graph: Map<String, List<String>>): Int {
        var totalPaths = 0
        val q = arrayDequeOf(State("start"))

        while (q.isNotEmpty()) {
            val state = q.poll()
            val current = state.current
            val counts = state.counts
            if (current == "end") {
                totalPaths++
            } else {
                val newCounts = counts.copy()
                if (current.isSmall()) {
                    newCounts.increment(current)
                }

                val neighbors = graph[current]?.filter { neighbor ->
                    when {
                        neighbor == "start" -> false
                        newCounts.any { it.value == 2L } -> {
                            newCounts[neighbor] < 1L
                        }
                        else -> {
                            newCounts[neighbor] < 2L
                        }
                    }
                } ?: emptyList()
                for (next in neighbors) {
                    q.add(State(next, newCounts))
                }
            }
        }
        return totalPaths
    }

    val graph = File("inputs/2021/input.txt").useLines { lines ->
        lines.flatMap { line ->
            line.split("-").let { listOf(it[0] to it[1], it[1] to it[0]) }
        }.groupBy({ it.first }) { it.second }
    }

    println(part1(graph))
    println(part2(graph))
}