package aoc2022

import java.io.File


fun main() {

    val cipher1 = "ABC".zip("RPS").toMap()
    val cipher2 = "XYZ".zip("RPS").toMap()
    val winner = "RPS".zip("PSR").toMap()
    val loser = winner.entries.associate { it.value to it.key } 
    val handValues = "RPS".zip("123").associate { it.first to it.second.digitToInt() }

    fun scoreRound(hand1: Char, hand2: Char): Int {
        val handValue = handValues[hand2]!!
        val score = when {
            hand1 == hand2 -> 3
            hand2 == winner[hand1]!! -> 6
            else -> 0
        }
        return handValue + score
    }

    fun parseInput(): List<Pair<Char, Char>> {
        return File("input.txt").useLines { lines ->
            lines.map { line ->
                line.split(" ").let { it[0][0] to it[1][0] }
            }.toList()
        }
    }

    fun part1() {
        val moves = parseInput().map { (code1, code2) ->
            cipher1[code1]!! to cipher2[code2]!!
        }

        val score = moves.sumOf { (h1, h2) ->
            scoreRound(h1, h2)
        }

        println("score: $score")
    }

    fun part2() {
        val moves = parseInput().map { (code1, code2) ->
            val hand1 = cipher1[code1]!!
            val hand2 = when (code2) {
                'X' -> loser[hand1]!!
                'Y' -> hand1
                'Z' -> winner[hand1]!!
                else -> throw Exception("invalid code: $code2")
            }
            Pair(hand1, hand2)
        }

        val score = moves.sumOf { (h1, h2) ->
            scoreRound(h1, h2)
        }

        println("score: $score")
    }

    part1()
    part2()
}