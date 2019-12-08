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
    return depth + children.map { nextNode -> countOrbits(tree, nextNode, depth + 1) }.sum()
}

private fun findPath(
        start: String,
        target: String,
        tree: Map<String, Set<String>>,
        currentNode: String = start,
        path: List<String> = listOf(start)
): List<String> {
    return if (currentNode == target) {
        path
    } else {
        val children = tree[currentNode] ?: emptySet()
        children.map { nextNode ->
            findPath(start, target, tree, nextNode, path + nextNode)
        }.firstOrNull {
            it.isNotEmpty()
        } ?: emptyList()
    }
}

private fun countTotalOrbits() {
    val orbits = parseInput()
    println(countOrbits(orbits))
}

private fun countOrbitalTransfers() {
    val orbits = parseInput()
    val ourPath = findPath("COM", "YOU", orbits)
    val santasPath = findPath("COM", "SAN", orbits)
    val commonAncestor = ourPath.zip(santasPath).takeWhile { it.first == it.second }.last().first
    val ourOrbitLength = ourPath.dropWhile { it != commonAncestor }.size - 2
    val santasOrbitLength = santasPath.dropWhile { it != commonAncestor }.size - 2
    println(ourOrbitLength + santasOrbitLength)
}

fun main() {
    countTotalOrbits()
    countOrbitalTransfers()
}