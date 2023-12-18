package aoc2023

import util.Direction
import util.Point
import util.move
import java.io.File
import java.util.PriorityQueue

fun main() {

    data class State(
        val pos: Point,
        val dir: Direction,
        val heatLoss: Int = 0,
        val turnCounter: Int = 1
    ) {
        fun heuristic(goal: Point): Int {
            return heatLoss + pos.mDist(goal)
        }
    }

    operator fun <T> List<List<T>>.contains(point: Point): Boolean {
        return point.y in this.indices && point.x in this[0].indices
    }

    operator fun <T> List<List<T>>.get(point: Point): T? {
        return getOrNull(point.y)?.getOrNull(point.x)
    }

    fun findMinPath(minStraightSteps: Int, maxStraighSteps: Int): Int {
        val grid = File("input.txt").readLines().map { line -> line.map { it.digitToInt() } }
        val goal = Point(grid[0].lastIndex, grid.lastIndex)
        val comparator = Comparator.comparingInt<State> { it.heuristic(goal) }

        val pq = PriorityQueue(comparator)
        val seenStates = mutableSetOf<Triple<Point, Direction, Int>>()

        pq.addAll(listOf(
                State(Point(0, 0), Direction.EAST),
                State(Point(0, 0), Direction.SOUTH))
        )

        while (pq.isNotEmpty()) {
            val state = pq.poll()

            if (state.pos == goal) {
                return state.heatLoss
            } else {
                val key = Triple(state.pos, state.dir, state.turnCounter)
                if (key in seenStates) continue
                seenStates.add(key)
            }

            val dirsAndCounters = when {
                state.turnCounter < minStraightSteps -> {
                    listOf(state.dir to state.turnCounter + 1)
                }
                state.turnCounter >= maxStraighSteps -> {
                    listOf(
                        state.dir.turnLeft() to 1,
                        state.dir.turnRight() to 1,
                    )
                }
                else -> {
                    listOf(
                        state.dir to state.turnCounter + 1,
                        state.dir.turnLeft() to 1,
                        state.dir.turnRight() to 1
                    )
                }
            }

            val nextStates = dirsAndCounters.map { (newDir, newCounter) ->
                val newPos = state.pos.move(newDir)
                val newHeatLoss = state.heatLoss + (grid[newPos] ?: 0)
                State(newPos, newDir, newHeatLoss, newCounter)
            }.filter {
                it.pos in grid
            }.filter {
                Triple(it.pos, it.dir, it.turnCounter) !in seenStates
            }

            pq.addAll(nextStates)
        }
        throw Exception("no path found")
    }

    fun part1() {
        println(findMinPath(0, 3))
    }

    fun part2() {
        println(findMinPath(4, 10))
    }

    part1()
    part2()
}
