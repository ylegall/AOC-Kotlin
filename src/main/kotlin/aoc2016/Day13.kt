package aoc2016

import aoc2016.Day13.minStepsToGoal
import aoc2016.Day13.reachableCellsUnder50
import util.Point
import java.util.*
import kotlin.Comparator


object Day13 {

    data class State(
            val pos: Point,
            val steps: Int = 0
    )

    private fun isOpen(x: Int, y: Int): Boolean {
        if (x < 0 || y < 0) {
            return false
        }
        val value = x * x + 3 * x + 2 * x * y + y + y * y + 1364
        val bits = Integer.bitCount(value)
        return bits % 2 == 0
    }

    fun minStepsToGoal(start: Point, goal: Point): Int {
        val seen = mutableSetOf<Point>()
        val q = PriorityQueue<State>(Comparator.comparingInt { it.steps + it.pos.mDist(goal) })
        q.add(State(start))
        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            if (pos !in seen) {
                if (pos == goal) {
                    return steps
                }
                seen.add(pos)
                val neighbors = pos.cardinalNeighbors().filter {
                    it !in seen && isOpen(it.x, it.y)
                }.map {
                    State(it, steps + 1)
                }
                q.addAll(neighbors)
            }
        }
        throw Exception("ran out of moves")
    }

    fun reachableCellsUnder50(start: Point): Int {
        var count = 0
        val seen = mutableSetOf<Point>()
        val q = PriorityQueue<State>(Comparator.comparingInt { it.steps })
        q.add(State(start))
        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            if (pos !in seen) {
                if (steps <= 50) {
                    count++
                }
                seen.add(pos)
                val neighbors = pos.cardinalNeighbors().filter {
                    it !in seen && isOpen(it.x, it.y)
                }.map {
                    State(it, steps + 1)
                }
                q.addAll(neighbors)
            }
        }
        return count
    }

    fun printGrid(seen: Set<Point> = emptySet()) {
        (0..30).forEach { x ->
            (0..30).forEach{ y ->
                val cell = Point(x, y)
                print(if (cell in seen) 'X' else if (isOpen(x, y)) '.' else '=')
            }.also { println() }
        }
    }
}

fun main() {
    val start = Point(1, 1)
    val end = Point(31, 39)

    println(minStepsToGoal(start, end))
    println(reachableCellsUnder50(start))
}