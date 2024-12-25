package aoc2024

import util.Point
import util.contains
import java.io.File
import java.util.PriorityQueue

fun main() {
    val len = 70

    val input = File("input.txt").readLines().map { line ->
        line.split(",").let {
            Point(it[0].toInt(), it[1].toInt())
        }}

    data class State(
        val pos: Point,
        val steps: Int = 0
    )

    fun minSteps(walls: Set<Point>): Int {
        val start = Point(0, 0)
        val goal = Point(len, len)
        val bounds = Pair(start, Point(goal.x + 1, goal.y + 1))
        val seen = mutableSetOf<Point>()
        val q = PriorityQueue(compareBy<State> { it.steps + it.pos.mDist(goal) })
        q.add(State(start))

        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            if (pos == goal) {
                return steps
            } else {
                if (pos in seen) continue
                seen.add(pos)

                val neighbors = pos.cardinalNeighbors()
                    .filter { it !in walls && it in bounds && it !in seen }
                    .map { State(it, steps + 1) }
                q.addAll(neighbors)
            }
        }
        throw Exception("goal not found")
    }

    // part 1
    val result = minSteps(input.take(1024).toSet())
    println(result)

    // part 2
    // found this index manually, but binary search would be the proper way
    println(input[3036].let { "${it.x},${it.y}" })
}