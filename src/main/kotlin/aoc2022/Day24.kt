package aoc2022

import util.Direction
import util.Direction.*
import util.Point
import java.io.File
import java.util.PriorityQueue

fun main() {

    data class Blizzard(
        val pos: Point,
        val dir: Direction
    )

    class Input(
        lines: List<String>
    ) {
        val bounds = Point(1, 1) to Point(lines[1].length - 2, lines.size - 2)
        val start = Point(lines[0].indexOfFirst { it != '#' }, 0)
        val goal = Point(lines.last().indexOfFirst { it != '#' }, lines.lastIndex)
        val rows = lines.size - 2
        val cols = lines[0].length - 2
        val blizzards = lines.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, char ->
                val pos = Point(colIndex, rowIndex)
                when (char) {
                    '^' -> Blizzard(pos, NORTH)
                    '>' -> Blizzard(pos, EAST)
                    'v' -> Blizzard(pos, SOUTH)
                    '<' -> Blizzard(pos, WEST)
                    else -> null
                }
            }.toSet()
        }
    }

    fun computeOpenPoints(steps: Int, input: Input): Set<Point> {
        val blizzards = input.blizzards.asSequence().map { (pos, dir) ->
            when (dir) {
                NORTH -> pos.copy(y = (pos.y - 1 - steps).mod(input.rows) + 1)
                EAST  -> pos.copy(x = (pos.x - 1 + steps).mod(input.cols) + 1)
                SOUTH -> pos.copy(y = (pos.y - 1 + steps).mod(input.rows) + 1)
                WEST  -> pos.copy(x = (pos.x - 1 - steps).mod(input.cols) + 1)
            }
        }.toSet()

        val allPoints = (input.bounds.first.y .. input.bounds.second.y).flatMap { row ->
            (input.bounds.first.x .. input.bounds.second.x).map { col -> Point(col, row) }
        }.toSet()

        return (allPoints - blizzards) + input.start + input.goal
    }

    operator fun Pair<Point, Point>.contains(pos: Point): Boolean {
        return pos.x in first.x..second.x && pos.y in first.y..second.y
    }

    data class State(
        val pos: Point,
        val steps: Int = 0,
    ) {
        fun heuristic(goal: Point): Int {
            return steps + pos.mDist(goal)
        }
    }

    fun minPath(
        start: State,
        goal: Point,
        input: Input,
        blizzardCache: MutableMap<Int, Set<Point>>
    ): Int {
        val q = PriorityQueue<State> { s1, s2 -> s1.heuristic(goal).compareTo(s2.heuristic(goal)) }
        val seen = mutableSetOf<State>()
        q.add(start)

        while (q.isNotEmpty()) {
            val state = q.poll()
            if (state in seen) continue
            if (state.pos == goal) {
                return state.steps
            }
            seen.add(state)

            val nextStep = state.steps + 1
            val key = nextStep % (input.cols * input.rows)
            val openPoints = blizzardCache[key] ?: computeOpenPoints(key, input)
            val nextStates = state.pos.cardinalNeighbors().plus(state.pos).asSequence()
                .filter { it in input.bounds || it == input.start || it == input.goal }
                .filter { it in openPoints }
                .map { newPos -> State(newPos, nextStep) }
                .filter { it !in seen }

            q.addAll(nextStates)
        }
        return -1
    }

    fun minStepsRoundTrips() {
        val cache = mutableMapOf<Int, Set<Point>>()

        val input = Input(File("input.txt").readLines())
        val start = State(input.start, 0)
        val steps1 = minPath(start, input.goal, input, cache)
        println(steps1)

        val start2 = State(input.goal, steps1)
        val steps2 = minPath(start2, input.start, input, cache)

        val start3 = State(input.start, steps2)
        val steps3 = minPath(start3, input.goal, input, cache)
        println(steps3)
    }

    minStepsRoundTrips()
}
