package aoc2019

import java.io.File

private fun parseInput(): Map<String, Set<String>> {
    return File("inputs/2019/6.txt").useLines { lines ->
        lines.map { it.split(")") }.groupBy(
                { it[0] },
                { it[1] }
        ).mapValues { it.value.toSet() }
    }
}

private fun countOrbits(
        tree: Map<String, Set<String>>,
        currentNode: String = "COM",
        depth: Int = 0
): Int {
    val children = tree[currentNode] ?: emptySet()
    return depth + children.map { countOrbits(tree, it, depth + 1) }.sum()
}

private fun countTotalOrbits() {
    val orbits = parseInput()
    println(countOrbits(orbits))
}

fun main() {
    countTotalOrbits()
}