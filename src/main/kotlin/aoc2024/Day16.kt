package aoc2024

import util.Direction
import util.Direction.*
import util.Point
import util.move
import java.io.File
import java.util.PriorityQueue
import kotlin.math.min

fun main() {

    class Input(
        val start: Point,
        val end: Point,
        val walls: Set<Point>
    )

    fun parseInput(): Input {
        var start = Point(0, 0)
        var end = Point(0, 0)
        val walls = mutableSetOf<Point>()
        val lines = File("input.txt").readLines()
        for (r in lines.indices) {
            for (c in lines[r].indices) {
                val char = lines[r][c]
                val point = Point(c, r)
                when (char) {
                    '#' -> walls.add(point)
                    'S' -> start = point
                    'E' -> end = point
                }
            }
        }
        return Input(start, end, walls)
    }

    data class Heading(
        val pos: Point,
        val dir: Direction
    )

    data class State(
        val pos: Point,
        val dir: Direction,
        val score: Int
    )
    fun State.heading() = Heading(pos, dir)

    fun State.neighbors(input: Input): List<State> {
        val neighbors = mutableListOf<State>()
        val forwardPos = pos.move(dir)
        if (forwardPos !in input.walls) {
            neighbors.add(State(forwardPos, dir, score + 1))
        }
        val rightPos = pos.move(dir.turnRight())
        if (rightPos !in input.walls) {
            neighbors.add(State(rightPos, dir.turnRight(), score + 1001))
        }
        val leftPos = pos.move(dir.turnLeft())
        if (leftPos !in input.walls) {
            neighbors.add(State(leftPos, dir.turnLeft(), score + 1001))
        }
        return neighbors
    }

    fun findMinScoreAllPoints(
        start: Point,
        end: Point,
        input: Input
    ): Map<Point, Int> {
        val q = PriorityQueue<State>(compareBy { it.score })
        val initialState = State(start, EAST, 0)
        q.add(initialState)
        var minScore: Int? = null
        val scoreMap = mutableMapOf<Point, Int>()
        val seen = mutableSetOf<Heading>()

        while (q.isNotEmpty()) {
            val state = q.poll()
            if (state.pos == end) {
                if (minScore == null) {
                    minScore = state.score
                    scoreMap[state.pos] = state.score
                } else if (state.score > minScore) {
                    return scoreMap
                }
            } else {
                val heading = state.heading()
                if (heading in seen) {
                    continue
                }
                seen.add(heading)
                scoreMap[state.pos] = min(state.score, (scoreMap[state.pos] ?: Int.MAX_VALUE))

                val nextStates = state.neighbors(input)
                q.addAll(nextStates)
            }
        }
        throw Exception("goal not found")
    }

    fun findAllPaths(
        input: Input,
        scoreMap: Map<Point, Int>,
        minScore: Int
    ): List<List<Point>> {
        val paths = mutableListOf<List<Point>>()

        fun recurse(state: State, path: List<Point>, seen: Set<Heading>) {
            if (state.pos !in scoreMap) return
            if (scoreMap[state.pos]!! + state.score > minScore) return
            if (state.score > minScore) return
            if (state.pos == input.start) {
                paths.add(path + input.start)
                return
            }

            val neighbors = state.neighbors(input).filter { it.heading() !in seen }
            neighbors.forEach {
                recurse(it, path + state.pos, seen + it.heading())
            }
        }
        for (dir in entries) {
            recurse(State(input.end, dir, 0), emptyList(), emptySet())
        }
        return paths
    }

    val input = parseInput()

    // part 1
    val scoreMap = findMinScoreAllPoints(input.start, input.end, input)
    val minScore = scoreMap[input.end]!!
    println(minScore)

    // part 2 (reverse search)
    val allPoints = findAllPaths(input, scoreMap, minScore).flatten().distinct()
    println(allPoints.size)
}
