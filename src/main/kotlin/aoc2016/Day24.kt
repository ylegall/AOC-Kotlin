package aoc2016

import util.Point
import util.permutations
import java.io.File
import java.util.ArrayDeque

private object Day24 {

    class Maze(private val data: List<String>) {

        val targets = data.indices.flatMap { row ->
            data[row].indices.mapNotNull { col ->
                val char = data[row][col]
                if (char.isDigit()) {
                    char to Point(row, col)
                } else {
                    null
                }
            }
        }.sortedBy {
            it.first
        }.map {
            it.second
        }

        fun Point.neighbors() = cardinalNeighbors().filter { get(it) != '#' }

        private fun get(point: Point) = when {
            point.x !in data.indices -> '#'
            point.y !in data[point.x].indices -> '#'
            else -> data[point.x][point.y]
        }

        private data class SearchState(val pos: Point, val steps: Int = 0)

        fun minStepsStartingFrom(start: Point): Map<Point, Int> {
            val seen = HashSet<Point>()
            val minStepsToTargets = HashMap<Point, Int>()
            val q = ArrayDeque<SearchState>().apply { add(SearchState(start)) }
            while (q.isNotEmpty()) {
                val (currentPos, currentSteps) = q.poll()
                if (currentPos in seen) continue
                if (currentPos in targets && currentPos !in minStepsToTargets) {
                    minStepsToTargets[currentPos] = currentSteps
                    if (minStepsToTargets.size == targets.size) {
                        break
                    }
                }
                seen.add(currentPos)
                val nextStates = currentPos.neighbors().filter {
                    it !in seen
                }.map {
                    SearchState(it, currentSteps + 1)
                }
                q.addAll(nextStates)
            }
            return minStepsToTargets
        }

        fun findAllShortestPaths(): Map<Pair<Point, Point>, Int> {
            val allPaths = HashMap<Pair<Point, Point>, Int>()
            for (i in 0 until targets.size - 1) {
                val start = targets[i]
                val paths = minStepsStartingFrom(start)
                for ((target, steps) in paths) {
                    allPaths[start to target] = steps
                    allPaths[target to start] = steps
                }
            }
            return allPaths
        }

        fun shortestPathThroughAllPoints(
                pathLengths: Map<Pair<Point, Point>, Int>,
                returnToStart: Boolean = false
        ): Int {
            return (1 until targets.size).permutations().map {
                listOf(0) + it + if (returnToStart) listOf(0) else emptyList()
            }.map { permutation ->
                permutation.windowed(2) { segment ->
                    val start = targets[segment[0]]
                    val end = targets[segment[1]]
                    pathLengths[start to end] ?: pathLengths[end to start]!!
                }.sum()
            }.min()!!
        }

    }

    private fun parseInput(): Maze {
        return Maze(File("inputs/2016/24.txt").readLines())
    }

    fun run() {
        val maze = parseInput()
        val allPathLengths = maze.findAllShortestPaths()
        println(maze.shortestPathThroughAllPoints(allPathLengths))
        println(maze.shortestPathThroughAllPoints(allPathLengths, returnToStart = true))
    }
}

fun main() {
    Day24.run()
}
