package aoc2022

import util.Direction
import util.Direction.EAST
import util.Direction.WEST
import util.Direction.NORTH
import util.Direction.SOUTH
import util.Point
import util.move
import java.io.File

fun main() {

    fun Point.neighbors() = cardinalNeighbors() + listOf(
        Point(x + 1, y + 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1),
        Point(x - 1, y - 1),
    )

    fun Iterable<Point>.direction(dir: Direction) = when (dir) {
        NORTH -> sortedBy { it.y }.take(3)
        SOUTH -> sortedBy { it.y }.takeLast(3)
        EAST  -> sortedBy { it.x }.takeLast(3)
        WEST  -> sortedBy { it.x }.take(3)
    }

    val initialPositions = File("input.txt").useLines { lines ->
        lines.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, char ->
                if (char == '#') { Point(colIndex, rowIndex) } else { null }
            }
        }.toSet()
    }

    fun Point.proposedDirection(positions: Set<Point>, directions: List<Direction>): Point {
        val neighbors = neighbors()
        val proposedDir = if (neighbors.count { it in positions } == 0) {
            null
        } else {
            directions.firstOrNull { dir ->
                val neighborsByDir = neighbors.direction(dir)
                neighborsByDir.count { it in positions } == 0
            }
        }
        return if (proposedDir != null) {
            this.move(proposedDir)
        } else {
            this
        }
    }

    data class State(
        val positions: Set<Point>,
        val directions: List<Direction>
    )

    fun simulateRound(state: State): State {
        val (positions, directions) = state
        val proposedMoves = positions.associateWith { it.proposedDirection(positions, directions) }
        val collisionFree = proposedMoves.entries.groupBy { it.value }.filterValues { it.size == 1 }.keys
        val newPositions = proposedMoves.map { (p1, p2) -> if (p2 in collisionFree) p2 else p1 }.toSet()
        val newDirections = directions.drop(1) + directions.first()

        return State(
            newPositions,
            newDirections
        )
    }

    fun part1() {
        var state = State(initialPositions, listOf(NORTH, SOUTH, WEST, EAST))
        repeat(10) {
            state = simulateRound(state)
        }
        val minX = state.positions.minOf { it.x }
        val minY = state.positions.minOf { it.y }
        val maxX = state.positions.maxOf { it.x }
        val maxY = state.positions.maxOf { it.y }
        val emptyPositions = (minY .. maxY).sumOf { row ->
            (minX .. maxX).count { col -> Point(col, row) !in state.positions }
        }
        println(emptyPositions)
    }

    fun part2() {
        var last = initialPositions
        var state = State(initialPositions, listOf(NORTH, SOUTH, WEST, EAST))
        var i = 0
        while (true) {
            state = simulateRound(state)
            i++
            if (state.positions == last) {
                break
            }
            last = state.positions
        }
        println(i)
    }

    part1()
    part2()
}
