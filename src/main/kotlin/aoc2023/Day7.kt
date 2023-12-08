package aoc2023

import aoc2023.Day7.HandType
import aoc2023.Day7.HandType.*
import java.io.File

private object Day7 {
    enum class HandType {
        FIVE_KIND,
        FOUR_KIND,
        FULL_HOUSE,
        THREE_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }
}

fun main() {

    val labels = "A,K,Q,J,T,9,8,7,6,5,4,3,2".split(",").map { it[0] }
    val labelsNoJoker = labels.filter { it != 'J' }

    val strengthMap = labels
        .mapIndexed { index, label -> label to (index) }
        .toMap()

    val strengthMapWildcard = ((labels - 'J') + 'J')
        .mapIndexed { index, label -> label to (index) }
        .toMap()

    data class Card(
        val label: Char,
        val allowWildcards: Boolean = false
    ): Comparable<Card> {

        fun strength() = if (allowWildcards) {
            strengthMapWildcard[label]!!
        } else {
            strengthMap[label]!!
        }

        override fun compareTo(other: Card): Int {
            return strength().compareTo(other.strength())
        }

        override fun toString() = label.toString()
    }

    data class Hand(
        val cards: List<Card>,
        val allowWildcards: Boolean = false
    ): Comparable<Hand> {

        init { assert(cards.size == 5) }

        constructor(str: String, allowWildcards: Boolean = false): this(str.map { Card(it, allowWildcards) }, allowWildcards)

        val handType = if (allowWildcards) {
            bestWildcardHand().classifyHandType()
        } else {
            classifyHandType()
        }

        override fun toString() = cards.joinToString("")

        fun expandWildcards(): Set<Hand> {
            val results = mutableSetOf<Hand>()
            fun recurse(currentCards: List<Card>) {
                if (currentCards.size == 5) {
                    results.add(Hand(currentCards))
                } else {
                    val nextLabel = cards[currentCards.size].label
                    if (nextLabel == 'J') {
                        for (label in labelsNoJoker) {
                            recurse(currentCards + Card(label, allowWildcards))
                        }
                    } else {
                        recurse(currentCards + Card(nextLabel, allowWildcards))
                    }
                }
            }
            recurse(emptyList())
            return results
        }

        fun bestWildcardHand() = expandWildcards().minOf { it }

        fun classifyHandType(): HandType {
            val counts = cards.groupingBy { it.label }.eachCount()
            val maxCount = counts.values.max()
            val minCount = counts.values.min()

            return when {
                counts.size == 1 && counts.toList().first().second == 5 -> {
                    FIVE_KIND
                }
                maxCount == 4 -> {
                    FOUR_KIND
                }
                maxCount == 3 -> {
                    when (minCount) {
                        2 -> FULL_HOUSE
                        1 -> THREE_KIND
                        else -> throw Exception("invalid min count: $minCount")
                    }
                }
                maxCount == 2 -> {
                    when (counts.values.count { it == 1 }) {
                        1 -> TWO_PAIR
                        3 -> ONE_PAIR
                        else -> throw Exception("invalid count: $minCount")
                    }
                }
                else -> {
                    HIGH_CARD
                }
            }
        }

        override fun compareTo(other: Hand): Int {
            return if (handType == other.handType) {
                cards.zip(other.cards).map {
                    it.first.compareTo(it.second)
                }.firstOrNull { it != 0 } ?: 0
            } else {
                handType.ordinal.compareTo(other.handType.ordinal)
            }
        }
    }

    fun part1() {
        val result = File("input.txt").readLines()
            .map { line ->
                val tokens = line.split(" ")
                Hand(tokens[0]) to tokens[1].toInt()
            }.sortedByDescending {
                it.first
            }.mapIndexed { index, pair ->
                (index + 1) * pair.second
            }.sum()

        println(result)
    }

    fun part2() {
        val result = File("input.txt").readLines()
            .map { line ->
                val tokens = line.split(" ")
                val hand = Hand(tokens[0], allowWildcards = true)
                hand to tokens[1].toLong()
            }.sortedByDescending {
                it.first
            }.mapIndexed { index, pair ->
                (index + 1) * pair.second
            }.sum()

        println(result)
    }

    part1()
    part2()
}
