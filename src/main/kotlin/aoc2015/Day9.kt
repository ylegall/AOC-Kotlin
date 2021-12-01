package aoc2015

import util.input
import util.permutations

private fun tourLengths(distances: Map<Pair<String, String>, Int>, cities: List<String>): List<Int> {
    return cities.permutations().map { cityList ->
        cityList.zipWithNext().map { cityPair -> distances[cityPair]!! }.sum()
    }
}

fun main() {
    val distances = HashMap<Pair<String, String>, Int>()
    input("inputs/2015/9.txt").use { lines ->
        for (line in lines) {
            val tokens = line.split(" ")
            val dist = tokens[4].toInt()
            distances[Pair(tokens[0], tokens[2])] = dist
            distances[Pair(tokens[2], tokens[0])] = dist
        }
    }
    val cities = distances.keys.map { it.first }.distinct()
    val tourLengths = tourLengths(distances, cities)
    println(tourLengths.minOrNull())
    println(tourLengths.maxOrNull())
}