package aoc2019

import util.Point
import util.arrayDequeOf
import util.get
import util.findPoint
import java.io.File
import java.util.ArrayDeque

private object Day18_TopologicalSort {

    private val maze = File("inputs/2019/18.txt").readLines()
    private val start = maze.map { it.toList() }.findPoint { it == '@' }!!
    private val points = mutableMapOf<Char, Point>()
    private val distances = mutableMapOf<Pair<Char, Char>, Int>()
    private val dependencies = scanMaze()

    private data class SearchState(
            val pos: Point,
            val steps: Int = 0,
            val doors: Set<Char> = emptySet()
    )

    private fun scanMaze(): Map<Char, Set<Char>> {
        val dependencies = mutableMapOf<Char, Set<Char>>()
        val seen = HashSet<Point>()
        val queue = ArrayDeque<SearchState>()
        queue.add(SearchState(start))
        while (queue.isNotEmpty()) {
            val (pos, steps, doors) = queue.poll()
            seen.add(pos)
            val tile = maze[pos]
            if (tile in 'a'..'z') {
                dependencies[tile] = doors
                points[tile] = pos
                distances['@' to tile] = steps
            }

            val newDoors = if (tile in 'A'..'Z') {
                doors + tile.toLowerCase()
            } else {
                doors
            }

            val neighbors = pos.cardinalNeighbors().filter {
                maze[it] != '#' && it !in seen
            }.map {
                SearchState(it, steps + 1, newDoors)
            }
            queue.addAll(neighbors)
        }
        return dependencies
    }

    private fun minStepsBetween(start: Char, end: Char): Int {
        if (start to end in distances) return distances[start to end]!!
        if (end to start in distances) return distances[end to start]!!

        val seen = HashSet<Point>()
        val queue = arrayDequeOf(points[start]!! to 0)
        while (queue.isNotEmpty()) {
            val (pos, steps) = queue.poll()
            if (points[end]!! == pos) {
                distances[start to end] = steps
                return steps
            }
            seen.add(pos)
            val next = pos.cardinalNeighbors().filter {
                maze[it] != '#' && it !in seen
            }.map {
                it to steps + 1
            }
            queue.addAll(next)
        }
        throw Exception("path not found between $start and $end")
    }

    val pathCosts = mutableListOf<Int>()
    fun findMinPathWithDependencies(
            path: String = "@",
            totalSteps: Int = 0,
            remainingDependencies: Map<Char, Set<Char>> = dependencies
    ) {
        if (remainingDependencies.isEmpty()) {
            println("found path $path with cost $totalSteps")
            pathCosts.add(totalSteps)
            return
        }

        val openKeys = remainingDependencies.filter { it.value.isEmpty() }.keys
        for (key in openKeys) {
            findMinPathWithDependencies(
                    path + key,
                    totalSteps + minStepsBetween(path.last(), key),
                    remainingDependencies.minus(key).mapValues { it.value.minus(key) }
            )
        }
    }

    fun run() {
        println(dependencies)
        println(points)
        findMinPathWithDependencies()
        println(pathCosts.min())
    }
}

fun main() {
    Day18_TopologicalSort.run()
}