package aoc2015

import util.input

private fun permutations(cities: List<String>): List<List<String>> {
    return permutations(cities, linkedSetOf(), arrayListOf())
}

private fun permutations(
        cities: List<String>,
        current: LinkedHashSet<String>,
        results: ArrayList<List<String>>
): List<List<String>> {
    val nextCities = cities.filter { it !in current }

    if (nextCities.isEmpty()) {
        results.add(current.toList())
    } else {
        for (city in nextCities) {
            current.add(city)
            permutations(cities, current, results)
            current.remove(city)
        }
    }
    return results
}

fun tourLengths(distances: Map<Pair<String, String>, Int>, cities: List<String>): List<Int> {
    return permutations(cities).map { cityList ->
        cityList.zipWithNext().map { distances[it]!! }.sum()
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
    println(tourLengths.min())
    println(tourLengths.max())
}