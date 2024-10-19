package aoc2019

import java.io.File

fun main() {

    fun parseInput(): Map<String, Set<String>> {
        return File("input.txt").useLines { lines ->
            lines.map { it.split(")") }.groupBy(
                    { it[0] },
                    { it[1] }
            ).mapValues { it.value.toSet() }
        }
    }

    fun countOrbits(
        tree: Map<String, Set<String>>,
        currentNode: String = "COM",
        depth: Int = 0
    ): Int {
        val children = tree[currentNode] ?: emptySet()
        return depth + children.sumOf { nextNode -> countOrbits(tree, nextNode, depth + 1) }
    }

    fun findPath(
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

    fun countTotalOrbits(orbits: Map<String, Set<String>>) {
        println(countOrbits(orbits))
    }

    fun countOrbitalTransfers(orbits: Map<String, Set<String>>) {
        val ourPath = findPath("COM", "YOU", orbits)
        val santasPath = findPath("COM", "SAN", orbits)
        val commonAncestor = ourPath.zip(santasPath).takeWhile { it.first == it.second }.last().first
        val ourOrbitLength = ourPath.dropWhile { it != commonAncestor }.size - 2
        val santasOrbitLength = santasPath.dropWhile { it != commonAncestor }.size - 2
        println(ourOrbitLength + santasOrbitLength)
    }

    val orbits = parseInput()
    countTotalOrbits(orbits)
    countOrbitalTransfers(orbits)
}