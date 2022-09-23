package aoc2021


fun main() {

    val start = Pair(4, 8)
//    val start = Pair(7, 10)

    data class Player(
        var pos: Int,
        var score: Int = 0
    )

    fun part1(): Long {
        var turn = 0
        val players = listOf(
            Player(start.first - 1),
            Player(start.second - 1)
        )
        var d = 0

        while (players.all { it.score < 1000 }) {
            val player = players[turn % 2]
            val rollValue = (d .. d + 2).asSequence()
                .map { (it % 100) + 1 }.sum()
            d = (d + 3) % 100
            player.pos = (player.pos + rollValue) % 10
            player.score += player.pos + 1
            turn++
        }

        val losingScore = players.minOf { it.score }
        println("turn = $turn")
        println("lossing score = $losingScore")
        println("rolls ${turn * 3}")
        return losingScore * (turn * 3L)
    }

    class GameState(
        val players: Pair<Player, Player>,
        val d: Int = 0,
        val turn: Int = 0,
    )


    fun part2() {

        fun playRecursive(game: GameState): Pair<Long, Long> {
            val (p1, p2) = game.players
            if (p1.score >= 21) {
                return 1L to 0L
            }
            if (p2.score >= 21) {
                return 0L to 1L
            }

            val player = if (game.turn % 2 == 0) p1 else p2

            val totalWins = (1 .. 3).map { rollValue ->
                val newPosition = (player.pos + rollValue) % 10
                val newScore = player.score + newPosition + 1
                val newPlayer = Player(newPosition, newScore)
                val newPlayers = if (game.turn % 2 == 0) newPlayer to p2 else p1 to newPlayer
                val newGame = GameState(newPlayers, game.turn + 1)
                playRecursive(newGame)
            }.reduce { a, b -> a.first + b.first to a.second + b.second }
            return totalWins
        }

        val game = GameState(Player(start.first - 1) to Player(start.second - 1))
        val totals = playRecursive(game)
        println(totals)
    }

//    println(part1())
    println(part2())
}
