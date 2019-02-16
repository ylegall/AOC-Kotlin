package aoc2015

import util.input
import kotlin.streams.toList

private fun String.countExpansions(key: String, expansion: String): Set<String> {
    return (0 until length).filter { start ->
        substring(start).startsWith(key)
    }.map { start ->
        substring(0, start) + expansion + substring(start + key.length)
    }.toSet()
}

private fun String.countDistinctSingleExpansions(expansions: Map<String, List<String>>): Int {
    return expansions.entries.flatMap { expansion ->
        expansion.value.map { countExpansions(expansion.key, it) }
    }.reduce { set1, set2 -> set1 + set2 }.size
}

private fun String.countOf(target: String): Int {
    return Regex(target).findAll(this).count()
}

fun main() {
    input("inputs/2015/19.txt").use {
        val lines = it.toList()
        val expansions = lines.dropLast(2).map { line ->
            line.split(" => ")
        }.groupBy(
                { key -> key.first() },
                { value -> value.last() }
        )

        val molecule = lines.last()
        println(molecule.countDistinctSingleExpansions(expansions))

        println(molecule.let {
            it.count { it.isUpperCase() } - it.countOf("Ar") - it.countOf("Rn") - (2 * it.count { it == 'Y' }) - 1
        })
    }

}