package aoc2016

import java.util.BitSet
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day11_new {

    private const val UP = 1
    private const val DOWN = -1
    private const val GENS = 8
    private const val SIZE = 2 * GENS

    private class Floor(vararg items: Int): BitSet(32) {
        init {
            items.forEach { set(it) }
        }

        fun isValid(): Boolean {
            val generatorIds = generators().toSet()
            return generatorIds.isEmpty() || chips().all { hasGeneratorForChip(it) }
        }

        fun hasGeneratorForChip(id: Int) = get(id - GENS)

        fun generators() = (0 until GENS).filter { get(it) }

        fun chips() = (GENS until SIZE).filter { get(it) }

        fun items() = (0 until SIZE).filter { get(it) }

        fun add(idx: Int?): Floor = if (idx != null) {
            (this.clone() as Floor).also { it.set(idx) }
        } else {
            this
        }

        fun sub(idx: Int?): Floor = if (idx != null) {
            (this.clone() as Floor).also { it.set(idx, false) }
        } else {
            this
        }
    }

    private data class FloorState(
            val pos: Int,
            val floors: List<Floor>
    ) {
        fun isGoal() = floors.dropLast(1).all { it.isEmpty }

        fun isValid() = floors.all { it.isValid() }.and(!floors[pos].isEmpty)

        fun floorsBelowAreEmpty() = floors.subList(0, pos).all { it.isEmpty }

        fun nextStates(): List<FloorState> {
            val choices = floors[pos].items()
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

        private fun nextStates(firstChoice: Int, secondChoice: Int?): List<FloorState> {
            return when(pos) {
                0               -> listOf(moveFloors(UP, firstChoice, secondChoice))
                floors.size - 1 -> listOf(moveFloors(DOWN, firstChoice, secondChoice))
                else            -> {
                    if (floorsBelowAreEmpty()) {
                        listOf(moveFloors(UP, firstChoice, secondChoice))
                    } else {
                        listOf(moveFloors(UP, firstChoice, secondChoice), moveFloors(DOWN, firstChoice, secondChoice))
                    }
                }
            }
        }

        private fun moveFloors(direction: Int, firstChoice: Int, secondChoice: Int?): FloorState {
            val newFloors = floors.indices.map { index ->
                when (index) {
                    pos             -> floors[index].sub(firstChoice).sub(secondChoice)
                    pos + direction -> floors[index].add(firstChoice).add(secondChoice)
                    else            -> floors[index]
                }
            }
            return FloorState(pos + direction, newFloors)
        }

//        private val idMap = HashMap<Int, Int>()
//        private fun searialize(): String {
//            var id = 1
//            var bits = 0L
//            idMap.clear()
//            floors.forEachIndexed { floorIndex, floor ->
//                var counter = 0
//                for (gen in floor.generators()) {
//
//                }
//            }
//            for (floorIndex in floors.indices) {
//                val floor = floors[floorIndex]
//                var counter = 0
//                for (gen in floor.generators()) {
//
//                }
//            }
//        }
    }

    fun minSteps(start: FloorState): Int {
        var states = listOf(start)
        val seen = HashSet<FloorState>()
        var steps = 0
        while (true) {
            if (states.any { it.isGoal() }) {
                return steps
            }
            seen.addAll(states)

            val nextStates = states.flatMap {
                it.nextStates()
            }.filter {
                it !in seen
            }.distinct()

            if (nextStates.isEmpty()) throw Exception("ran out of states")

            states = nextStates
            steps++
        }
    }

    @ExperimentalTime
    fun run() {
        val start = FloorState(0,
                listOf(
                        Floor(0, 1, 9, 2, 3, 11, 4, 12),
                        Floor(8, 10),
                        Floor(),
                        Floor()
                )
        )
        println(measureTimedValue { minSteps(start) })
    }

}

@ExperimentalTime
fun main() {
    Day11_new.run()
}
