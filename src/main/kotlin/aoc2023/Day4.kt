package aoc2023

import util.MutableCounter
import java.io.File


fun main() {

    val inputCards = File("input.txt").useLines { lines ->
        lines.mapIndexed { index, line ->
            val parts = line.split(": ", " | ")
            val winningNumbers = parts[1].trim().split(" ")
                .asSequence()
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .toSet()

            val numbers = parts[2].split(" ")
                .asSequence()
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .toSet()

            index to winningNumbers.intersect(numbers).size
        }.toList()
    }

    fun part1() {
        println(inputCards.sumOf { (_, matches) ->
            if (matches > 0) 1 shl (matches-1) else 0
        })
    }

    fun part2() {
        val cardCounts = MutableCounter<Int>()
        inputCards.forEach { (index, matches) ->
            cardCounts.increment(index)
            val currentCopies = cardCounts[index]
            for (id in index+1 .. index + matches) {
                cardCounts.increment(id, currentCopies)
            }
        }
        println(cardCounts.values.sum())
    }

    part1()
    part2()
}