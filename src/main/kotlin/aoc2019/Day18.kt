package aoc2019

import util.Point
import util.arrayDequeOf
import util.findPoint
import util.get
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day18 {

    private val maze = File("inputs/2019/18.txt").readLines()
    private val start = maze.map { it.toList() }.findPoint { it == '@' }!!
    private val points = mutableMapOf<Char, Point>()
    private val distances = mutableMapOf<Pair<Char, Char>, Int>()
    private val dependencies = scanMaze()
    private val allKeys = points.keys - '@'

    private data class SearchState(
            val pos: Point,
            val steps: Int = 0,
            val doors: Set<Char> = emptySet()
    )

    private fun scanMaze(): Map<Char, Set<Char>> {
        val dependencies = mutableMapOf<Char, Set<Char>>()
        val seen = HashSet<Point>()
        val queue = arrayDequeOf(SearchState(start))
        
        while (queue.isNotEmpty()) {
            val (pos, steps, doors) = queue.poll()
            seen.add(pos)
            val tile = maze[pos]
            if (tile in 'a'..'z') {
                dependencies[tile] = doors
                points[tile] = pos
                distances['@' to tile] = steps
            }

            val newDoors = if (tile in 'A'..'Z') {
                doors + tile.toLowerCase()
            } else {
                doors
            }

            val neighbors = pos.cardinalNeighbors().filter {
                maze[it] != '#' && it !in seen
            }.map {
                SearchState(it, steps + 1, newDoors)
            }
            queue.addAll(neighbors)
        }
        return dependencies
    }

    private fun minStepsBetween(start: Char, end: Char): Int {
        if (start to end in distances) return distances[start to end]!!
        if (end to start in distances) return distances[end to start]!!

        val seen = HashSet<Point>()
        val queue = arrayDequeOf(points[start]!! to 0)
        while (queue.isNotEmpty()) {
            val (pos, steps) = queue.poll()
            if (points[end]!! == pos) {
                distances[start to end] = steps
                return steps
            }
            seen.add(pos)
            val next = pos.cardinalNeighbors().filter {
                maze[it] != '#' && it !in seen
            }.map {
                it to steps + 1
            }
            queue.addAll(next)
        }
        throw Exception("path not found between $start and $end")
    }

    data class KeySet(private val bits: Int = 0) {
     
        private fun keyId(key: Char) = key - 'a'
        
        val size; get() = Integer.bitCount(bits)
        
        operator fun contains(key: Char) = (bits and (1 shl keyId(key))) != 0
        
        fun withKey(key: Char) = KeySet(bits or (1 shl keyId(key)))
    }
    

    fun minPathForAllKeys(
            currentKey: Char = '@',
            currentSteps: Int = 0,
            keys: KeySet = KeySet(),
            cache: HashMap<Pair<Char, KeySet>, Int> = hashMapOf()
    ): Int {
        val cacheKey = Pair(currentKey, keys)
        return when {
            keys.size == allKeys.size -> currentSteps
            cacheKey in cache         -> currentSteps + cache[cacheKey]!!
            else -> {
                dependencies.filter { (nextKey, requiredKeys) ->
                    nextKey !in keys && requiredKeys.all { it in keys }
                }.map { (nextKey, _) ->
                    minPathForAllKeys(
                            nextKey,
                            currentSteps + minStepsBetween(currentKey, nextKey),
                            keys.withKey(nextKey),
                            cache
                    )
                }.min()!!.also { steps ->
                    cache[cacheKey] = steps - currentSteps
                }
            }
        }
    }
    
    @ExperimentalTime
    fun run() {
        println(measureTimedValue { minPathForAllKeys() })
    }
}

@ExperimentalTime
fun main() {
    Day18.run()
}