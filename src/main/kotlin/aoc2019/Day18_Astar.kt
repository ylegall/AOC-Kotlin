package aoc2019

import util.Point
import util.get
import util.findPoint
import java.io.File
import java.util.PriorityQueue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day18_Astar {

    private val maze = File("inputs/2019/18.txt").readLines()
    private val startPoint = maze.map { it.toList() }.findPoint { it == '@' }!!
    private val totalKeys = maze.sumBy { it.count { it in 'a'..'z' } }

    private fun Int.keyCount() = Integer.bitCount(this)

    private fun Char.keyId() = this - 'a'

    private fun Int.withKey(key: Char) = this or (1 shl key.keyId())

    private fun Int.hasKeyForDoor(door: Char) = (this and (1 shl door.toLowerCase().keyId())) != 0

    private data class SearchState(
            val pos: Point,
            val steps: Int = 0,
            val keys: Int = 0
    )

    private val searchStateComparator = compareBy<SearchState>( { it.steps}, { -it.keys.keyCount() } )

    private fun minStepsToCollectAllKeys(): Int {
        val seen = HashSet<Pair<Point, Int>>()
        val queue = PriorityQueue<SearchState>(searchStateComparator)
        queue.add(SearchState(startPoint))
        while (queue.isNotEmpty()) {
            val (pos, steps, keys) = queue.poll()
            seen.add(pos to keys)

            val tile = maze[pos]

            if (tile in 'A'..'Z' && !keys.hasKeyForDoor(tile)) {
                continue
            }

            val newKeys = if (tile in 'a'..'z') {
                keys.withKey(tile)
            } else {
                keys
            }

            if (newKeys.keyCount() == totalKeys) {
                return steps
            }

            val neighbors = pos.cardinalNeighbors().filter {
                maze[it] != '#' && (it to newKeys) !in seen
            }.map {
                SearchState(it, steps + 1, newKeys)
            }

            queue.addAll(neighbors)
        }
        throw Exception("ran out of states to search")
    }

    @ExperimentalTime
    fun run() {
        val result = measureTimedValue {
            minStepsToCollectAllKeys()
        }
        println(result)
    }
}

@ExperimentalTime
fun main() {
    Day18_Astar.run()
}