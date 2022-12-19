package aoc2022

import util.Point
import util.findFirstPoint
import java.io.File
import java.util.*

fun main() {

    val input = File("input.txt").readLines()

    data class State(
        val pos: Point,
        val steps: Int
    )

    operator fun List<String>.contains(pos: Point) = when {
        pos.x !in this.indices        -> false
        pos.y !in this[pos.x].indices -> false
        else -> true
    }

    fun List<String>.height(pos: Point): Int {
        return when (val c = this[pos.x][pos.y]) {
            'S' -> 0
            'E' -> 25
            else -> c - 'a'
        }
    }

    fun search(
        start: Point,
        goalCondition: (Point) -> Boolean,
        heightFilter: (Point, Int) -> Boolean
    ): Int? {
        val seen = mutableSetOf<Point>()

        val q = PriorityQueue<State> { s1, s2 -> s1.steps.compareTo(s2.steps) }
        q.add(State(start, 0))

        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()!!
            if (pos in seen) continue

            if (goalCondition(pos)) {
                return steps
            }
            seen.add(pos)

            val currentHeight = input.height(pos)

            val nextMoves = pos.cardinalNeighbors()
                .filter { neighborPos -> neighborPos !in seen }
                .filter { neighborPos -> neighborPos in input }
                .filter { neighborPos -> heightFilter(neighborPos, currentHeight) }
                .map { neighborPos -> State(neighborPos, steps + 1) }
            q.addAll(nextMoves)
        }
        return null
    }

    fun part1() {
        val start = input.findFirstPoint { it == 'S' }!!
        val goal = input.findFirstPoint { it == 'E' }!!
        val minSteps = search(
            start,
            goalCondition = { it == goal },
            heightFilter = { nextPos: Point, currentHeight: Int -> input.height(nextPos) - currentHeight <= 1 }
        )
        println(minSteps)
    }

    fun part2() {
        val start = input.findFirstPoint { it == 'E' }!!
        val minSteps = search(
            start,
            goalCondition = { input[it.x][it.y] == 'a' },
            heightFilter = { nextPos: Point, currentHeight: Int -> input.height(nextPos) - currentHeight >= -1 }
        )
        println(minSteps)
    }

    part1()
    part2()
}