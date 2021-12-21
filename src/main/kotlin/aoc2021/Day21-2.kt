package aoc2021

import kotlin.math.max


fun main() {

    data class Player(
        val pos: Int,
        val score: Long = 0
    )

    data class GameState(
        val players: List<Player>,
        val d: Int = 0,
        val turns: Int = 0
    )

    fun part2(): Long {

        val cache = mutableMapOf<GameState, Pair<Long, Long>>()

        fun playGame(state: GameState): Pair<Long, Long> {
            if (state in cache) { return cache[state]!! }
            val p1 = state.players[0]
            val p2 = state.players[1]
            if (p1.score >= 21) { return 1L to 0 }
            if (p2.score >= 21) { return 0L to 1L }

            val player = if ((state.turns/3) % 2 == 0) p1 else p2
            val result = (1 .. 3).map { roll ->
                val newD = (state.d + roll) % 3
                val newTurns = state.turns + 1
                val newPosition = (player.pos + newD + 1) % 10
                val newScore = if (state.turns % 3 == 2) {
                    player.score + newPosition + 1
                } else {
                    player.score
                }
                val newPlayer = Player(newPosition, newScore)
                val newPlayers = if (player === p1) listOf(newPlayer, p2) else listOf(p1, newPlayer)
                val nextState = GameState(newPlayers, newD, newTurns)
                playGame(nextState)
            }.reduce { a, b ->
                a.first + b.first to a.second + b.second
            }

            cache[state] = result
            return result
        }

        val players = listOf(
//            Player(3),
//            Player(7),
            Player(6),
            Player(9),
        )
        val game = GameState(players)
        val result = playGame(game)
        println(result)
        return max(result.first, result.second)
    }

    println(part2())
}
