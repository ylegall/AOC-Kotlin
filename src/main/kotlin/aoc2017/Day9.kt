package aoc2017

import util.input
import kotlin.streams.asSequence


private object Day9 {

    private const val OPEN_GROUP    = '{'
    private const val CLOSE_GROUP   = '}'
    private const val ESCAPE        = '!'
    private const val OPEN_GARBAGE  = '<'
    private const val CLOSE_GARBAGE = '>'

    fun calculateScore(charSequence: Sequence<Char>): Pair<Int, Int> {
        var score = 0
        var garbageCount = 0
        var nestLevel = 0
        var isEscape = false
        var isGarbage = false

        for (char in charSequence) {
            when {
                isEscape   -> isEscape = false
                isGarbage  -> when (char) {
                    ESCAPE        -> isEscape = true
                    CLOSE_GARBAGE -> isGarbage = false
                    else          -> garbageCount++
                }
                else       -> when(char) {
                    OPEN_GROUP   -> nestLevel++
                    CLOSE_GROUP  -> {
                        score += nestLevel
                        nestLevel--
                    }
                    OPEN_GARBAGE -> isGarbage = true
                }
            }
        }

        return Pair(score, garbageCount)
    }

    fun run() {
        input("inputs/2017/9.txt").use {
            val sequence = it.asSequence().flatMap { line -> line.asSequence() }
            println(calculateScore(sequence))
        }
    }
}

fun main() {
    Day9.run()
}