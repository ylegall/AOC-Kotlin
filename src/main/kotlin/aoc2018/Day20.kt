package aoc2018

import util.Direction
import util.Direction.*
import util.Point
import util.enumSetOf
import util.move
import java.io.File
import java.util.*

private object Day20 {

    private fun walkGraph(graph: Map<Point, EnumSet<Direction>>) {
        val seen = HashSet<Point>()
        val roomsBeyond1000 = HashSet<Point>()
        var frontier = setOf(Point(0, 0))
        var steps = 0
        while (frontier.isNotEmpty()) {
            if (steps >= 1000) {
                roomsBeyond1000.addAll(frontier)
            }
            seen.addAll(frontier)
            frontier = frontier.flatMap { point ->
                graph[point]?.map { dir -> point.move(dir) } ?: emptyList()
            }.filter {
                it !in seen
            }.toSet()
            steps++
        }
        println(steps - 1)
        println(roomsBeyond1000.size)
    }

    private fun parseInput(): Map<Point, EnumSet<Direction>> {
        val text = File("inputs/2018/20.txt").readText().trim()
        val stack = ArrayDeque<Point>()
        var currentPoint = Point(0, 0)
        val graph = HashMap<Point, EnumSet<Direction>>()

        for (char in text) {
            when(char) {
                '(' -> stack.push(currentPoint)
                '|' -> currentPoint = stack.peek()
                ')' -> currentPoint = stack.pop()
            }

            val nextDirection = when(char) {
                'N' -> NORTH
                'E' -> EAST
                'S' -> SOUTH
                'W' -> WEST
                else -> null
            }

            if (nextDirection != null) {
                val neighbors = graph.getOrPut(currentPoint) { enumSetOf() }
                neighbors.add(nextDirection)
                val nextPoint = currentPoint.move(nextDirection)
                val otherNeighbors = graph.getOrPut(nextPoint) { enumSetOf() }
                otherNeighbors.add(nextDirection.reverse())
                currentPoint = nextPoint
            }
        }
        return graph
    }

    fun run() {
        val graph = parseInput()
        walkGraph(graph)
    }
}

fun main() {
    Day20.run()
}