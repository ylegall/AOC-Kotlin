package aoc2024

import util.*
import java.io.File

fun main() {

    data class State(
        val pos: Point,
        val dir: Direction
    )

    operator fun List<String>.contains(point: Point): Boolean {
        return point.y in this.indices && point.x in this[0].indices
    }

    class SimResult(
        val seenStates: Set<State>,
        val endingType: String
    )

    fun simulate(
        startPos: Point,
        startDir: Direction,
        grid: List<List<Char>>
    ): SimResult {
        val seenStates = mutableSetOf<State>()
        var state = State(startPos, startDir)
        while (true) {
            if (state in seenStates) {
                return SimResult(seenStates, "loop")
            }
            seenStates.add(state)
            val nextPos = state.pos.move(state.dir)
            if (nextPos !in grid) {
                return SimResult(seenStates, "exit")
            }
            val nextChar = grid[nextPos]
            state = when (nextChar) {
                '#' -> State(state.pos, state.dir.turnRight())
                else -> State(nextPos, state.dir)
            }
        }
    }

    fun part1() {
        val grid = File("input.txt").readLines()
        val startPos = grid.findFirstPoint('^')!!
        val charGrid = grid.map { it.toMutableList() }
        val pathResult = simulate(startPos, Direction.NORTH, charGrid)

        println(pathResult.seenStates.map { it.pos }.toSet().size)

        val states = pathResult.seenStates.drop(1)
        val obstacleCount = states.asSequence().map {
            it.pos
        }.filter { pos ->
            val prevChar = charGrid[pos.y][pos.x]
            charGrid[pos.y][pos.x] = '#'
            val result = simulate(startPos, Direction.NORTH, charGrid)
            charGrid[pos.y][pos.x] = prevChar
            result.endingType == "loop"
        }.toSet().size
        println(obstacleCount)
    }

    part1()

}