package aoc2025

import java.io.File

private object Day10 {

    data class Machine(
        val lights: List<Int>,
        val buttons: List<List<Int>>,
        val joltages: List<Int>
    )

    fun parseInput(): List<Machine> {
        fun String.unwrap(n: Int=1) = substring(n, length-n)

        val machines = File("input.txt").readLines().map { line ->
            val tokens = line.split(" ")
            val lights = tokens[0].unwrap().map { if (it == '.') 0 else 1 }
            val joltageTarget = tokens.last().unwrap().split(",").map { it.toInt() }

            val buttonWires = tokens.subList(1, tokens.size-1).map { token ->
                token.unwrap().split(",").map { it.toInt() }
            }
            Machine(lights, buttonWires, joltageTarget)
        }
        return machines
    }

    fun List<Int>.decrement(other: List<Int>): List<Int> {
        val result = this.toMutableList()
        other.forEach { idx -> result[idx]-- }
        return result
    }

    fun findButtonsForParityPattern(
        target: List<Int>,
        buttons: List<List<Int>>,
    ): List<List<List<Int>>> {
        val results = mutableSetOf<List<Int>>()

        fun recurse(counts: List<Int>, idx: Int, pressedButtons: List<Int>) {
            if (idx >= buttons.size) {
                if (counts.all { it.mod(2) == 0 }) {
                    results.add(pressedButtons)
                }
            } else {
                val newCounts = counts.decrement(buttons[idx])
                recurse(newCounts, idx + 1, pressedButtons + idx)
                recurse(counts, idx + 1, pressedButtons)
            }
        }

        recurse(target, 0, emptyList())
        return results.map { buttonIndices -> buttonIndices.map { buttons[it] }}
    }

    fun minPressesForJoltage(machine: Machine): Long {
        val cache = mutableMapOf<List<Int>, Long?>()
        val parityCache = mutableMapOf<List<Int>, List<List<List<Int>>>>()

        fun recurse(counts: List<Int>): Long? {
            return when {
                counts in cache -> cache[counts]
                counts.all { it == 0 } -> 0L
                else -> {
                    val parityPattern = counts.map { it % 2 }
                    val buttonsList = if (parityPattern in parityCache) {
                        parityCache[parityPattern]!!
                    } else {
                        findButtonsForParityPattern(parityPattern, machine.buttons).also {
                            parityCache[parityPattern] = it
                        }
                    }
                    var minPresses: Long? = null
                    for (buttons in buttonsList) {
                        val newCounts = buttons.fold(counts) { acc, button -> acc.decrement(button) }
                        if (newCounts.any { it < 0 }) continue
                        minPresses = listOfNotNull(
                            minPresses,
                            recurse(newCounts.map { it / 2 })?.times(2L)?.plus(buttons.size)
                        ).minOrNull()
                    }
                    minPresses.also { cache[counts] = it }
                }
            }
        }
        return recurse(machine.joltages)!!
    }

    fun part1(machines: List<Machine>) {
        val result = machines.sumOf { machine ->
            findButtonsForParityPattern(machine.lights, machine.buttons)
                .minOf { it.size }
        }
        println(result)
    }

    fun part2(machines: List<Machine>) {
        val result = machines.sumOf { machine ->
            minPressesForJoltage(machine)
        }
        println(result)
    }
}

fun main() {
    val input = Day10.parseInput()
    Day10.part1(input)
    Day10.part2(input)
}