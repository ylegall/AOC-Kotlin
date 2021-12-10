package aoc2021

import util.arrayDequeOf
import java.io.File

private enum class Result {
    INCOMPLETE,
    CURRUPT,
    OK
}

fun main() {

    val syntaxErrorScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    val autoCompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun Char.match() = when (this) {
        '{' -> '}'
        '(' -> ')'
        '[' -> ']'
        '<' -> '>'
        '}' -> '{'
        ')' -> '('
        ']' -> '['
        '>' -> '<'
        else -> throw Exception("bad char")
    }

    val openChars = "[{<(".toSet()

    fun evalLine(line: String): Pair<Result, String> {
        val stack = arrayDequeOf<Char>()
        for (c in line) {
            if (c in openChars) {
                stack.add(c)
            } else {
                if (stack.isEmpty()) {
                    return Result.CURRUPT to c.toString()
                }
                val top = stack.pollLast()
                if (top != c.match()) {
                    return Result.CURRUPT to c.toString()
                }
            }
        }

        return if (stack.isEmpty()) {
            Result.OK to ""
        } else {
            Result.INCOMPLETE to stack.joinToString("")
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (result, remaining) = evalLine(line)
            if (result == Result.CURRUPT) {
                syntaxErrorScores[remaining[0]] ?: 0
            } else {
                0
            }
        }
    }

    fun autoCompleteScore(chars: String): Long {
        return chars.map { char ->
            autoCompleteScores[char]!!
        }.fold(0L) { sum, score ->
            (sum * 5L) + score
        }
    }

    fun part2(input: List<String>): Long {
        val scores = input.asSequence().map { line ->
            evalLine(line)
        }.filter { (result, _) ->
            result == Result.INCOMPLETE
        }.map { (_, remaining) ->
            autoCompleteScore(remaining.reversed().map { it.match() }.joinToString(""))
        }.toList()

        return scores.sorted()[scores.size / 2]
    }

    val input = File("inputs/2021/input.txt").readLines()

    println(part1(input))
    println(part2(input))
}