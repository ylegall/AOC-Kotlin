package aoc2021

import util.replace
import java.io.File
import kotlin.math.max

private object Day21 {

    const val SIZE = 10

    fun parseInput(): Pair<Int, Int> {
        return File("input.txt").readLines().let { lines ->
            Pair(
                lines.first().let { it.drop(it.lastIndexOf(' ')+1).toInt() },
                lines.last().let { it.drop(it.lastIndexOf(' ')+1).toInt() }
            )
        }
    }

    fun simulateGameDeterministic(
        input: Pair<Int, Int>,
        goal: Int
    ): Int {
        var turn = 0
        var rolls = 0
        val positions = mutableListOf(input.first, input.second)
        val scores = mutableListOf(0, 0)
        var diePosition = 0
        while (scores.none { it >= goal }) {
            val index = turn % 2
            val rollValue = (diePosition ..diePosition + 2).map { it % 100 + 1 }
            diePosition = rollValue.last()
            positions[index] = (positions[index] + rollValue.sum() - 1) % SIZE + 1
            scores[index] += positions[index]
            turn++
            rolls += 3
        }
        return scores.minOrNull()!! * rolls
    }

    data class Player(
        val position: Int,
        val score: Int = 0
    )

    fun tripleSumFrequencies(): Map<Int, Int> {
        val counts = mutableMapOf<Int, Int>()
        for (i in 1 .. 3) {
            for (j in 1 .. 3) {
                for (k in 1 .. 3) {
                    val sum = i + j + k
                    counts[sum] = (counts[sum] ?: 0) + 1
                }
            }
        }
        return counts
    }

    data class State(
        val players: List<Player>,
        val turn: Int
    )

    fun simulateGameQuantum(
        input: Pair<Int, Int>,
        goal: Int
    ): Pair<Long, Long> {
        val tripleSumFrequencies = tripleSumFrequencies()
        val cache = mutableMapOf<State, Pair<Long, Long>>()

        fun recurse(state: State): Pair<Long, Long> {
            return when {
                state.players[0].score >= goal -> 1L to 0L
                state.players[1].score >= goal -> 0L to 1L
                else -> cache.getOrPut(state) {
                    val playerIndex = state.turn % 2
                    val player = state.players[playerIndex]
                    tripleSumFrequencies.map { (rollSum, count) ->
                        val newPosition = (player.position + rollSum - 1) % SIZE + 1
                        val newScore = player.score + newPosition
                        val newPlayers = state.players.replace(playerIndex, Player(newPosition, newScore))
                        val newState = State(newPlayers, state.turn + 1)
                        recurse(newState).let {
                            it.first * count to it.second * count
                        }
                    }.reduce { a, b ->
                        a.first + b.first to a.second + b.second
                    }
                }
            }
        }

        val initialState = State(listOf(Player(input.first), Player(input.second)), 0)
        return recurse(initialState)
    }

    fun part1(input: Pair<Int, Int>) {
        println(simulateGameDeterministic(input, 1000))
    }

    fun part2(input: Pair<Int, Int>) {
        val wins = simulateGameQuantum(input, 21)
        println(max(wins.first, wins.second))
    }
}

fun main() {
    val input = Day21.parseInput()
    Day21.part1(input)
    Day21.part2(input)
}