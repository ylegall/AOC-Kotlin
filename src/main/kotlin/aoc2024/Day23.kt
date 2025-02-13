package aoc2024

import java.io.File

fun main() {

    val pairs = File("input.txt").readLines().map {
        val tokens = it.split("-")
        if (tokens[0] < tokens[1]) tokens[0] to tokens[1] else tokens[1] to tokens[0]
    }

    val graph = pairs.groupBy({ it.first }, { it.second }).mapValues { it.value.toSet() }

    fun findCliques(maxIterations: Int): Set<Set<String>> {
        var cliques = pairs.map { setOf(it.first, it.second) }.toMutableSet()
        var iteration = 0
        while (iteration < maxIterations) {
            val newCliques = mutableSetOf<Set<String>>()

            for (clique in cliques) {
                val candidates = clique.flatMap { graph[it] ?: emptySet() }.distinct() - clique
                for (candidate in candidates) {
                    // if (clique.all { candidate.connectedTo(it) }) {
                    if (clique.all { candidate in (graph[it] ?: emptySet()) }) {
                        newCliques.add(clique + candidate)
                    }
                }
            }

            cliques = newCliques
            iteration++

            if (cliques.size == 1) break
        }
        return cliques
    }

    // part 1
    val threeCliques = findCliques(1)
    val filtered = threeCliques.filter { it.any { it[0] == 't' } }
    println(filtered.size)

    // part 2
    val cliques = findCliques(13).first()
    println(cliques.sorted().joinToString(","))
}