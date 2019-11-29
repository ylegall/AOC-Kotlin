package aoc2018

import java.io.File

private object Day12 {

    private class PlantPots(
            private var state: Set<Int>,
            private var transitions: Set<String>
    ) {
        private var min: Int = state.min()!!
        private var max: Int = state.max()!!
        private var lastSum = 0

        private fun nextValueAtIndex(idx: Int): Boolean {
            val pattern = (idx - 2 .. idx + 2).joinToString("") { if (it in state) "#" else "." }
            return pattern in transitions
        }

        private fun computeNextGeneration() {
            state = (min - 2 .. max + 2).mapNotNull { idx ->
                if (nextValueAtIndex(idx)) {
                    max = max.coerceAtLeast(idx)
                    min = min.coerceAtMost(idx)
                    idx
                } else {
                    null
                }
            }.toSet()
        }

        fun countAtGeneration(gen: Long): Int {
            for (i in 1 .. gen) {
                computeNextGeneration()
                val sum = state.sum()
                println("$i: sum=$sum, diff=${sum - lastSum}")
                lastSum = sum
            }
            return state.sum()
        }
    }

    fun run() {
        val iterator = File("inputs/2018/12.txt").useLines { it.iterator() }

        val initialState = iterator.next().split(": ")[1].trim()
                .mapIndexed { i, c -> if (c == '#') i else -1 }
                .filter { it >= 0 }
                .toSet()

        iterator.next()

        val transitions = iterator.asSequence().map {
            it.split("=>").map { it.trim() }
        }.filter {
            it[1] == "#"
        }.map {
            it[0]
        }.toSet()

        println(PlantPots(initialState, transitions).countAtGeneration(20))
        println(PlantPots(initialState, transitions).countAtGeneration(111))
        println(3491 + 25 * (50000000000L - 100))
    }

}

fun main() {
    Day12.run()
}