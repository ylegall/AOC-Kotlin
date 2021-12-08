package aoc2021

import java.io.File

fun main() {

    fun part1(input: List<Int>, days: Int): Int {
        val fish = input.toMutableList()
        var i = 0
        repeat(days) {
            var newCount = 0
            for (i in fish.indices) {
                fish[i] = if (fish[i] == 0) {
                    newCount++
                    6
                } else {
                    fish[i] - 1
                }
            }

            fish.addAll(List(newCount) { 8 })
        }
        return fish.size
    }

    fun part2(input: List<Int>, days: Long): Long {
        val cache = mutableMapOf<Long, Long>()

        fun recursiveTotal(
            daysRemaining: Long
        ): Long {
            if (daysRemaining in cache) {
                return cache[daysRemaining]!!
            }

            val result = if (daysRemaining <= 0) {
                0L
            } else {
                val cycles = (daysRemaining - 1) / 7
                var sum = 0L
                for (i in 0 .. cycles) {
                    sum += 1 + recursiveTotal(daysRemaining - i * 7 - 9)
                }
                cache[daysRemaining] = sum
                sum
            }
            return result
        }

        return input.sumOf {
            recursiveTotal(days - it)
        } + input.size
    }

    val input = File("inputs/2021/6.txt").useLines { lines ->
        lines.first().split(",").map { it.toInt() }
    }

    println(part1(input, 80))
    println(part2(input, 256))

}