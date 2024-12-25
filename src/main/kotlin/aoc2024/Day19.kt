package aoc2024

import java.io.File
import kotlin.math.min

fun main() {

    class Input(
        val patterns: Set<String>,
        val lines: List<String>
    )

    fun parseInput(): Input {
        return File("input.txt").readLines().let { lines ->
            Input(
                lines.first().split(", ").toSet(),
                lines.drop(2)
            )
        }
    }

    val input = parseInput()
    val maxPatternSize = input.patterns.maxOf { it.length }

    fun validPattern(target: String, cache: MutableMap<String, Boolean>): Boolean {
        return when {
            target.isEmpty() -> true
            target in cache -> cache[target]!!
            else -> {
                val result = input.patterns.any { prefix ->
                    if (target.startsWith(prefix)) {
                        validPattern(target.drop(prefix.length), cache)
                    } else {
                        false
                    }
                }
                cache[target] = result
                result
            }
        }
    }

    fun String.prefixes(n: Int): Sequence<Pair<String, String>> {
        val limit = min(n, length)
        return if (limit <= 1) {
            emptySequence()
        } else {
            (1 .. limit).asSequence().map { mid ->
                this.take(mid) to this.drop(mid)
            }
        }
    }

    fun countWays(target: String, cache: MutableMap<String, Long>): Long {
        return when {
            target in cache -> cache[target]!!
            target.isEmpty() -> 1
            target.length == 1 -> if (target in input.patterns) 1 else 0
            else -> {
                val sum = target.prefixes(maxPatternSize).sumOf { (left, right) ->
                    if (left in input.patterns) countWays(right, cache) else 0
                }
                cache[target] = sum
                sum
            }
        }
    }

    fun part1() {
        val cache = mutableMapOf<String, Boolean>()
        val total = input.lines.count { validPattern(it, cache) }
        println(total)
    }

    fun part2() {
        val cache = mutableMapOf<String, Long>()
        val total = input.lines.sumOf { countWays(it, cache) }
        println(total)
    }

    part1()
    part2()

}
