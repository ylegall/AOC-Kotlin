package aoc2020

import java.io.File


private object Day22 {

    fun parseInput(lines: List<String>): Pair<List<Int>, List<Int>> {
        val cardsSequence = lines.asSequence()
        val list1 = cardsSequence.drop(1).takeWhile { it.isNotBlank() }.map { it.toInt() }.toList()
        val list2 = cardsSequence.dropWhile { it.isNotBlank() }.drop(2).map { it.toInt() }.toList()
        return list1 to list2
    }

    private fun computeScore(deck: List<Int>) = deck.reversed().mapIndexed { index, card ->
        ((index + 1) * card).toLong()
    }.sum()

    fun playGamePart1(
            list1: List<Int>,
            list2: List<Int>
    ): Long {
        val deck1 = ArrayDeque(list1)
        val deck2 = ArrayDeque(list2)
        while (true) {
            val card1 = deck1.removeFirst()
            val card2 = deck2.removeFirst()
            if (card1 > card2) {
                deck1.addLast(card1)
                deck1.addLast(card2)
            } else {
                deck2.addLast(card2)
                deck2.addLast(card1)
            }

            if (deck1.isEmpty()) {
                return computeScore(deck2)
            }
            if (deck2.isEmpty()) {
                return computeScore(deck1)
            }
        }
    }

    private data class GameState(
            val deck1: List<Int>,
            val deck2: List<Int>,
    )

    private data class GameResult(
            val deck: List<Int>,
            val winner: Int = 0
    )

    private fun recursiveRound(state: GameState): GameResult {
        val seen = mutableSetOf<Pair<List<Int>, List<Int>>>()
        var deck1 = state.deck1
        var deck2 = state.deck2
        while (true) {
            val key = deck1 to deck2
            if (key in seen) {
                return GameResult(deck1, 1)
            }
            seen.add(key)
            val card1 = deck1.first()
            val card2 = deck2.first()
            deck1 = deck1.drop(1)
            deck2 = deck2.drop(1)
            val (_, winner) = if (card1 <= deck1.size && card2 <= deck2.size) {
                val newDeck1 = deck1.take(card1)
                val newDeck2 = deck2.take(card2)
                recursiveRound(GameState(newDeck1, newDeck2))
            } else {
                if (card1 > card2) {
                    GameResult(deck1, 1)
                } else {
                    GameResult(deck2, 2)
                }
            }

            if (winner == 1) {
                deck1 = deck1 + listOf(card1, card2)
            } else {
                deck2 = deck2 + listOf(card2, card1)
            }

            if (deck1.isEmpty()) {
                return GameResult(deck2, 2)
            }
            if (deck2.isEmpty()) {
                return GameResult(deck1, 1)
            }
        }
    }

    fun playGamePart2(list1: List<Int>, list2: List<Int>): Long {
        val initialState = GameState(list1, list2)
        val (deck, _) = recursiveRound(initialState)
        return computeScore(deck)
    }

}

fun main() {
    val input = File("inputs/2020/22.txt").readLines()
    val (deck1, deck2) = Day22.parseInput(input)
    println(Day22.playGamePart1(deck1, deck2))
    println(Day22.playGamePart2(deck1, deck2))
}