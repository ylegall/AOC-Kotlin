package aoc2023

import java.io.File

private object Day12 {

    data class Report(
        val conditions: String,
        val damageSizes: List<Int>
    )

    fun parseInput() = File("input.txt").readLines().map { line ->
        val (conditions, damageSizes) = line.split(' ')
        Report(
            conditions,
            damageSizes.split(',').map { it.toInt() }
        )
    }

    fun possiblyAllDamaged(conditions: String, size: Int): Boolean {
        return conditions.take(size).none { it == '.' } &&
                conditions.getOrNull(size) != '#'
    }

    fun countPossibleArrangements(report: Report): Long {
        val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

        fun recurse(conditions: String, damageSizes: List<Int>): Long {
            val key = Pair(conditions, damageSizes)
            return cache.getOrPut(key) {
                val nextConditions = conditions.dropWhile { it == '.' }
                when {
                    nextConditions.isEmpty() -> if (damageSizes.isEmpty()) 1 else 0
                    damageSizes.isEmpty() -> if (nextConditions.none { it == '#' }) 1 else 0
                    else -> {
                        val nextSize = damageSizes.first()
                        when {
                            nextSize > nextConditions.length -> 0
                            nextConditions.first() == '#' -> {
                                if (possiblyAllDamaged(nextConditions, nextSize)) {
                                    recurse(nextConditions.drop(nextSize + 1), damageSizes.drop(1))
                                } else {
                                    0
                                }
                            }
                            else -> {
                                if (possiblyAllDamaged(nextConditions, nextSize)) {
                                    recurse(nextConditions.drop(nextSize + 1), damageSizes.drop(1)) +
                                            recurse(nextConditions.drop(1), damageSizes)
                                } else {
                                    recurse(nextConditions.drop(1), damageSizes)
                                }
                            }
                        }
                    }
                }
            }

        }

        return recurse(report.conditions, report.damageSizes)
    }

    fun part1(input: List<Report>) {
        println(input.sumOf { countPossibleArrangements(it) })
    }

    fun part2(input: List<Report>) {
        val updatedRecords = input.map { report ->
            Report(
                conditions = List(5) { report.conditions }.joinToString("?"),
                damageSizes = List(5) { report.damageSizes }.flatten()
            )
        }
        part1(updatedRecords)
    }
}

fun main() {
    val input = Day12.parseInput()
    Day12.part1(input)
    Day12.part2(input)
}