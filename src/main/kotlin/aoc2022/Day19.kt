
package aoc2022

import aoc2022.Day19.Material
import aoc2022.Day19.Material.*
import util.*
import java.io.File
import kotlin.math.ceil
import kotlin.math.max
import kotlin.streams.toList

private object Day19 {
    enum class Material {
        ORE, CLAY, OBSIDIAN, GEODE
    }
}

fun main() {

    class Blueprint(
        val conversions: Map<Material, Counter<Material>>
    ): Map<Material, Counter<Material>> by conversions {
        override operator fun get(key: Material): Counter<Material> = conversions[key]!!

        val maxRequired = Material.values().associateWith { type ->
            conversions.values.maxOf { it[type] }
        }.toCounter()
    }

    val numberPattern = """\d+""".toPattern()

    val inputBlueprints: List<Blueprint> = File("input.txt").useLines { lines ->
        lines.map { line ->
            val values = numberPattern.matcher(line).results().map { it.group().toInt() }.toList()
            val conversions = mapOf(
                ORE      to counterOf(ORE to values[1]),
                CLAY     to counterOf(ORE to values[2]),
                OBSIDIAN to counterOf(ORE to values[3], CLAY to values[4]),
                GEODE    to counterOf(ORE to values[5], OBSIDIAN to values[6]),
            )
            Blueprint(conversions)
        }.toList()
    }

    data class State(
        val time: Int,
        val robots: Counter<Material> = counterOf(Pair(ORE, 1)),
        val materials: Counter<Material> = counterOf(),
    ) {
        fun doNothing() = State(
            time = 0,
            robots = robots,
            materials = materials + (robots * time)
        )

        fun makeRobot(robotType: Material, blueprints: Blueprint): State? {
            val required = blueprints[robotType]
            val cyclesNeeded = required.map { (materialType, countRequired) ->
                val robotCount = robots[materialType]
                if (robotCount == 0) return null
                val existingMaterial = materials[materialType]
                val missing = (countRequired - existingMaterial).coerceAtLeast(0)
                ceil(missing.toDouble() / robotCount).toInt()
            }
            val cycles = cyclesNeeded.max()
            if (cycles >= time) return null
            val newTime = time - cycles - 1
            val newMaterials = materials + (robots * cycles - required)
            val newRobots = robots.increment(robotType)
            return State(newTime, newRobots, newMaterials + robots)
        }

        fun nextStates(blueprints: Blueprint): List<State> {
            return Material.values().reversed().asSequence()
                .filter { type ->
                    when {
                        time <= 1     -> false
                        time <= 2 && type != GEODE -> false
                        else -> {
                            type == GEODE || robots[type] < blueprints.maxRequired[type]
                        }
                    }
                }
                .mapNotNull { type ->
                    makeRobot(type, blueprints)
                }
                .let { if (time > 0) it + doNothing() else it }
                .toList()
        }

    }

    fun triangle(n: Int) = n * (n + 1) / 2

    fun maxGeodes(timeLimit: Int, blueprint: Blueprint): Int {
        var maxGeodes = 0

        fun recurse(state: State) {
            if (state.time < 0) return
            if (state.time == 0) {
                maxGeodes = max(maxGeodes, state.materials[GEODE])
            }
            val nextStates = state.nextStates(blueprint)
            for (next in nextStates) {
                val timeRemaining = state.time
                val maxPossible = state.materials[GEODE] +
                        state.robots[GEODE] * timeRemaining +
                        triangle(timeRemaining)

                if (maxPossible <= maxGeodes) {
                    // println("pruned")
                } else {
                    recurse(next)
                }
            }
        }

        recurse(State(timeLimit))

        return maxGeodes
    }

    fun part1() {
        val result = inputBlueprints.mapIndexed { index, blueprint ->
            (index + 1) * maxGeodes(24, blueprint)
        }.sum()
        println(result)
    }

    fun part2() {
        val result = inputBlueprints.take(3).mapIndexed { index, blueprint ->
            maxGeodes(32, blueprint)
        }.product()
        println(result)
    }

    part1()
    part2()
}
