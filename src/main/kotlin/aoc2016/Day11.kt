package aoc2016

import aoc2016.Day11.FloorState
import aoc2016.Day11.Moveable.Chip
import aoc2016.Day11.Moveable.Gen
import aoc2016.Day11.minSteps

private object Day11 {

    private const val UP = 1
    private const val DOWN = -1

    enum class ItemType {
        CHIP,
        GENERATOR
    }

    sealed class Moveable(open val id: Int, val type: ItemType) {
        data class Chip(override val id: Int): Moveable(id, ItemType.CHIP)
        data class Gen(override val id: Int): Moveable(id, ItemType.GENERATOR)
    }

    data class FloorState(
            val pos: Int,
            val floors: List<Set<Moveable>>
    ) {
        fun isGoal() = floors.dropLast(1).all { it.isEmpty() }

        fun isValid() = floors.all { isFloorValid(it) }.and(floors[pos].isNotEmpty())

        private fun isFloorValid(floor: Set<Moveable>): Boolean {
            val (chips, gens) = floor.partition { it.type == ItemType.CHIP }
            val generatorIds = gens.map { it.id }.toSet()
            return generatorIds.isEmpty() || chips.all { it.id in generatorIds }
        }

        fun nextStates(): List<FloorState> {
            val choices = floors[pos].toList()
            return choices.indices.flatMap { firstIdx ->
                (0 until choices.size + 1).map { secondIdx ->
                    choices[firstIdx] to choices.getOrNull(secondIdx)
                }
            }.flatMap { (firstItem, secondItem) ->
                nextStates(firstItem, secondItem)
            }.filter {
                it.isValid()
            }
        }

        private fun nextStates(firstChoice: Moveable, secondChoice: Moveable?): List<FloorState> {
            return when(pos) {
                0               -> listOf(moveFloors(UP, firstChoice, secondChoice))
                floors.size - 1 -> listOf(moveFloors(DOWN, firstChoice, secondChoice))
                else            -> listOf(moveFloors(UP, firstChoice, secondChoice), moveFloors(DOWN, firstChoice, secondChoice))
            }
        }

        private fun moveFloors(direction: Int, firstChoice: Moveable, secondChoice: Moveable?): FloorState {
            val newFloors = floors.indices.map { index ->
                when (index) {
                    pos             -> floors[index].subtract(firstChoice).subtract(secondChoice)
                    pos + direction -> floors[index].add(firstChoice).add(secondChoice)
                    else            -> floors[index]
                }
            }
            return FloorState(pos + direction, newFloors)
        }

        private fun <T> Set<T>.add(item: T?): Set<T> = if (item != null) this.plus(item) else this
        private fun <T> Set<T>.subtract(item: T?): Set<T> = if (item != null) this.minus(item) else this
    }

    fun minSteps(states: Collection<FloorState>, steps: Int = 0, seenStates: HashSet<FloorState> = HashSet()): Int {
        return if (states.any { it.isGoal() }) {
            steps
        } else {
            val nextStates = states
                    .filter { it !in seenStates}
                    .also { seenStates.addAll(it) }
                    .flatMap { it.nextStates() }
                    .distinct()
            minSteps(nextStates,steps + 1, seenStates)
        }
    }
}


fun main() {
    val initialState = FloorState(0,
            listOf(
                    setOf(Gen(1), Gen(2), Chip(2), Gen(3), Gen(4), Chip(4), Gen(5), Chip(5)),
                    setOf(Chip(1), Chip(3)),
                    setOf(),
                    setOf()
            )
    )
    println(minSteps(setOf(initialState)))
}