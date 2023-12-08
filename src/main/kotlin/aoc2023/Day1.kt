package aoc2023

import java.io.File

fun main() {

    val digitWords = listOf("one","two","three","four","five","six","seven","eight","nine")

    fun part1() {
        val sum = File("input.txt").useLines { lines ->
            lines.sumOf { line ->
                val digits = line.asSequence().filter { it.isDigit() }.map { it.digitToInt() }
                val first = digits.first()
                val last = digits.last()
                first * 10 + last
            }
        }
        println(sum)
    }

    fun part2() {
        val sum = File("input.txt").useLines { lines ->
            lines.sumOf { line ->
                val digits = line.indices.mapNotNull { startIndex ->
                    val suffix = line.substring(startIndex)
                    when {
                        suffix[0].isDigit() -> suffix[0].digitToInt()
                        else -> {
                            digitWords.mapIndexed { index, word -> word to index }
                                .firstOrNull { (word, _) -> suffix.startsWith(word) }
                                ?.let { (_, index) -> index+1 }
                        }
                    }
                }
                digits.first() * 10 + digits.last()
            }
        }
        println(sum)
    }

    part1()
    part2()
}
