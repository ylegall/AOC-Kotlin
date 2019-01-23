package aoc2015

import util.input
import util.permutations
import kotlin.streams.asSequence


private fun parseInput(lines: Sequence<String>): Map<Pair<String, String>, Int> {
    return lines.fold(hashMapOf()) { map, line ->
        val tokens = line.split(" ")
        val happiness = if (tokens[2] == "lose") -(tokens[3].toInt()) else tokens[3].toInt()
        map.apply {
            val pair = Pair(tokens.first(), tokens.last().dropLast(1))
            put(pair, happiness)
        }
    }
}

private fun maxHappiness(happinessMap: Map<Pair<String, String>, Int>): Int {
    val guests = happinessMap.keys.map { it.first }.distinct().toSet()

    return guests.permutations().map {
        it + it.first()
    }.map { seatList ->
        seatList.zipWithNext().map { happinessMap[it]!! }.sum() +
                seatList.reversed().zipWithNext().map { happinessMap[it]!! }.sum()
    }.max()!!
}

private fun <T> List<T>.dropAt(n: Int): List<T> = this.subList(0, n) + this.subList(n + 1, size)

private fun maxHappinessWithGap(happinessMap: Map<Pair<String, String>, Int>): Int {
    val guests = happinessMap.keys.map { it.first }.distinct().toSet()

    return guests.permutations().map {
        it + it.first()
    }.map { seatList ->
        (0 until seatList.size - 1).map { gap ->
            seatList.zipWithNext().dropAt(gap).map{ happinessMap[it]!! }.sum() +
                    seatList.reversed().zipWithNext().dropAt(seatList.size - 2 - gap).map { happinessMap[it]!! }.sum()
        }.max()!!
    }.max()!!
}

fun main() {
    val happinessMap = input("inputs/2015/13.txt").use { lines ->
        parseInput(lines.asSequence())
    }

    println(maxHappiness(happinessMap))
    println(maxHappinessWithGap(happinessMap))
}