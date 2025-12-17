package aoc2025

import java.io.File

private object Day11 {

    fun parseInput(): Map<String, List<String>> {
        return File("input.txt").readLines().associate { line ->
            val tokens = line.split(" ")
            tokens.first().dropLast(1) to tokens.drop(1)
        }
    }

    data class CacheKey(
        val device: String,
        val visited: Set<String>
    )

    fun countPaths(
        start: String,
        end: String,
        devices: Map<String, List<String>>,
        requiredDevices: Set<String>
    ): Long {
        val cache = mutableMapOf<CacheKey, Long>()

        fun recurse(device: String, seenDevices: Set<String>): Long {
            val key = CacheKey(device, seenDevices)
            return when {
                key in cache -> cache[key]!!
                else -> {
                    val result = if (device == end) {
                        if (seenDevices == requiredDevices) 1 else 0
                    } else {
                        val newSeenDevices = if (device in requiredDevices) seenDevices + device else seenDevices
                        devices[device]?.sumOf { recurse(it, newSeenDevices) } ?: 0L
                    }
                    result.also { cache[key] = it }
                }
            }
        }

        return recurse(start, emptySet())
    }
}



fun main() {
    val input = Day11.parseInput()
    // part 1
    println(Day11.countPaths("you", "out", input, emptySet()))
    // part 2
    println(Day11.countPaths("svr", "out", input, setOf("dac", "fft")))
}
