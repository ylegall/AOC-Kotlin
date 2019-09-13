package aoc2016

import aoc2016.Day13.Cell
import aoc2016.Day13.minStepsToGoal
import aoc2016.Day13.reachableCellsUnder50
import java.util.*
import kotlin.Comparator
import kotlin.math.abs


object Day13 {

    data class Cell(
            val x: Int,
            val y: Int
    ) {
        val isOpen: Boolean = isOpen(x, y)

        fun neighbors() = listOf(
                Cell(x + 1, y),
                Cell(x - 1, y),
                Cell(x    , y + 1),
                Cell(x    , y - 1)
        ).filter {
            it.isOpen
        }

        fun distanceFrom(other: Cell) = abs(x - other.x) + abs(y - other.y)
    }

    data class State(
        val pos: Cell,
        val steps: Int = 0
    )

    fun isOpen(x: Int, y: Int): Boolean {
        if (x < 0 || y < 0) {
            return false
        }
        val value = x * x + 3 * x + 2 * x * y + y + y * y + 1364
        val bits = Integer.bitCount(value)
        return bits % 2 == 0
    }

    fun minStepsToGoal(start: Cell, goal: Cell): Int {
        val seen = mutableSetOf<Cell>()
        val q = PriorityQueue<State>(Comparator.comparingInt { it.steps + it.pos.distanceFrom(goal) })
        q.add(State(start))
        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            if (pos !in seen) {
                if (pos == goal) {
                    return steps
                }
                seen.add(pos)
                q.addAll(pos.neighbors().filter { it !in seen }.map { State(it, steps + 1) })
            }
        }
        throw Exception("ran out of moves")
    }

    fun reachableCellsUnder50(start: Cell): Int {
        var count = 0
        val seen = mutableSetOf<Cell>()
        val q = PriorityQueue<State>(Comparator.comparingInt { it.steps })
        q.add(State(start))
        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            if (pos !in seen) {
                if (steps <= 50) {
                    count++
                }
                seen.add(pos)
                q.addAll(pos.neighbors().filter { it !in seen }.map { State(it, steps + 1) })
            }
        }
        return count
    }

    fun printGrid(seen: Set<Cell> = emptySet()) {
        (0..30).forEach { x ->
            (0..30).forEach{ y ->
                val cell = Cell(x, y)
                print(if (cell in seen) 'X' else if (cell.isOpen) '.' else '=')
            }.also { println() }
        }
    }
}

fun main() {
    val start = Cell(1, 1)
    val end = Cell(31, 39)

    println(minStepsToGoal(start, end))
    println(reachableCellsUnder50(start))
}