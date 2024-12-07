package aoc2024

import util.split
import java.io.File

fun main() {

    data class Input(
        val orderingRules: List<Pair<Int, Int>>,
        val pageNumbers: List<List<Int>>
    )

    fun parseInput(): Input {
        val parts = File("input.txt").readLines().split { it.isEmpty() }
        val orderingRules = parts[0].map { line ->
            line.split("|").let { it[0].toInt() to it[1].toInt() }
        }
        val pageNumbers = parts[1].map { line ->
            line.split(",").map { it.toInt() }
        }
        return Input(orderingRules, pageNumbers)
    }

    fun findAllLarger(nextMap: Map<Int, List<Int>>, root: Int): Set<Int> {
        val results = mutableSetOf<Int>()
        val q = ArrayDeque<Int>()
        q.addAll(nextMap[root] ?: emptyList())
        while (q.isNotEmpty()) {
            val next = q.removeFirst()
            if (next !in results) {
                results.add(next)
                q.addAll(nextMap[next]?.filter { it !in results } ?: emptyList())
            }
        }
        return results
    }

    fun sortUpdates(updates: List<Int>, input: Input): List<Int> {
        val updateSet = updates.toSet()
        val filteredRules = input.orderingRules.filter { it.first in updateSet && it.second in updateSet }
        val nextMap = filteredRules.groupBy({ it.first }, { it.second })
        val largerMap = updateSet.associateWith { findAllLarger(nextMap, it) }
        val comparator = Comparator<Int> { a, b ->
            when {
                b in largerMap[a]!! -> -1
                else -> 1
            }
        }
        return updates.sortedWith(comparator)
    }

    fun middleSumOfUpdates(valid: Boolean) {
        val input = parseInput()

        val result = input.pageNumbers.sumOf { numbers ->
            val sortedNumbers = sortUpdates(numbers, input)
            if (valid) {
                if (sortedNumbers == numbers) {
                    sortedNumbers[sortedNumbers.size/2]
                } else {
                    0
                }
            } else {
                if (sortedNumbers == numbers) {
                    0
                } else {
                    sortedNumbers[sortedNumbers.size/2]
                }
            }
        }
        println(result)
    }

    fun part1() {
        middleSumOfUpdates(true)
    }

    fun part2() {
        middleSumOfUpdates(false)
    }

    part1()
    part2()
}