package aoc2019

import util.Point
import util.arrayDequeOf
import util.findPoints
import util.get
import util.replace
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day18 {

    private data class SearchState(
            val pos: Point,
            val steps: Int = 0,
            val doors: KeySet = KeySet()
    )

    data class KeySet(private val bits: Int = 0) {

        private fun keyId(key: Char) = key - 'a'

        val size; get() = Integer.bitCount(bits)

        operator fun contains(key: Char) = (bits and (1 shl keyId(key))) != 0

        fun containsAll(otherKeys: KeySet) = (otherKeys.bits and bits) == otherKeys.bits

        fun withKey(key: Char) = KeySet(bits or (1 shl keyId(key)))
    }

    class MapInfo(
            val map: List<String>,
            val keyLocations: Map<Char, Point>,
            val keyQuadrants: Map<Char, Int>,
            val dependencies: Map<Char, KeySet>,
            val distances: MutableMap<Pair<Char, Char>, Int> = mutableMapOf()
    ) {
        val allKeys = keyLocations.keys - '@'

        fun minStepsBetween(start: Char, end: Char): Int {
            if (start to end in distances) return distances[start to end]!!
            if (end to start in distances) return distances[end to start]!!

            val seen = HashSet<Point>()
            val queue = arrayDequeOf(keyLocations[start]!! to 0)
            while (queue.isNotEmpty()) {
                val (pos, steps) = queue.poll()
                if (keyLocations[end]!! == pos) {
                    distances[start to end] = steps
                    return steps
                }
                seen.add(pos)
                val next = pos.cardinalNeighbors().filter {
                    map[it] != '#' && it !in seen
                }.map {
                    it to steps + 1
                }
                queue.addAll(next)
            }
            throw Exception("path not found between $start and $end")
        }
    }

    private fun scanMap(map: List<String>): MapInfo {
        val startPoints = map.map { it.toList() }.findPoints { it == '@' }
        val distances = mutableMapOf<Pair<Char, Char>, Int>()
        val dependencies = mutableMapOf<Char, KeySet>()
        val keyLocations = mutableMapOf<Char, Point>()
        val keyQuadrants = mutableMapOf<Char, Int>()
        val seen = HashSet<Point>()
        val queue = arrayDequeOf<SearchState>().apply {
            addAll(startPoints.map { SearchState(it) })
        }

        fun Point.getQuadrant() = when (y) {
            in 0 .. map.size / 2 -> when (x) {
                in 0 .. map[y].length / 2 -> 0
                else -> 1
            }
            else -> when (x) {
                in 0 .. map[y].length / 2 -> 2
                else -> 3
            }
        }

        while (queue.isNotEmpty()) {
            val (pos, steps, requiredKeys) = queue.poll()
            seen.add(pos)
            val item = map[pos]
            if (item in 'a'..'z') {
                dependencies[item] = requiredKeys
                keyLocations[item] = pos
                keyQuadrants[item] = pos.getQuadrant()
                distances['@' to item] = steps
            }

            val newRequiredKeys = if (item in 'A'..'Z') {
                requiredKeys.withKey(item)
            } else {
                requiredKeys
            }

            val neighbors = pos.cardinalNeighbors().filter {
                map[it] != '#' && it !in seen
            }.map {
                SearchState(it, steps + 1, newRequiredKeys)
            }
            queue.addAll(neighbors)
        }
        return MapInfo(map, keyLocations, keyQuadrants, dependencies, distances)
    }

    fun minPathForAllKeys(
            mapInfo: MapInfo,
            currentKey: Char = '@',
            currentSteps: Int = 0,
            keys: KeySet = KeySet(),
            cache: HashMap<Pair<Char, KeySet>, Int> = hashMapOf()
    ): Int {
        val cacheKey = Pair(currentKey, keys)
        return when {
            keys.size == mapInfo.allKeys.size -> currentSteps
            cacheKey in cache -> currentSteps + cache[cacheKey]!!
            else -> {
                mapInfo.dependencies.filter { (nextKey, requiredKeys) ->
                    nextKey !in keys && keys.containsAll(requiredKeys)
                }.map { (nextKey, _) ->
                    minPathForAllKeys(
                            mapInfo,
                            nextKey,
                            currentSteps + mapInfo.minStepsBetween(currentKey, nextKey),
                            keys.withKey(nextKey),
                            cache
                    )
                }.minOrNull()!!.also { steps ->
                    cache[cacheKey] = steps - currentSteps
                }
            }
        }
    }

    fun minPathForAllKeysIn4Quadrants(
            mapInfo: MapInfo,
            currentKeys: List<Char>,
            currentSteps: Int = 0,
            keys: KeySet = KeySet(),
            cache: HashMap<Pair<List<Char>, KeySet>, Int> = hashMapOf()
    ): Int {
        val cacheKey = Pair(currentKeys, keys)
        return when {
            keys.size == mapInfo.allKeys.size -> currentSteps
            cacheKey in cache -> currentSteps + cache[cacheKey]!!
            else -> {
                mapInfo.dependencies.filter { (nextKey, requiredKeys) ->
                    nextKey !in keys && keys.containsAll(requiredKeys)
                }.map { (nextKey, _) ->
                    val keyQuadrant = mapInfo.keyQuadrants[nextKey]!!
                    val currentKey = currentKeys[keyQuadrant]
                    val nextSteps = currentSteps + mapInfo.minStepsBetween(currentKey, nextKey)
                    val nextKeys = currentKeys.replace(keyQuadrant, nextKey)
                    minPathForAllKeysIn4Quadrants(
                            mapInfo,
                            nextKeys,
                            nextSteps,
                            keys.withKey(nextKey),
                            cache
                    )
                }.minOrNull()!!.also { steps ->
                    cache[cacheKey] = steps - currentSteps
                }
            }
        }
    }

    @ExperimentalTime
    fun part1() {
        val map = File("inputs/2019/18.txt").readLines()
        val mapInfo = scanMap(map)
        println(measureTimedValue { minPathForAllKeys(mapInfo) })
    }

    @ExperimentalTime
    fun part2() {
        val map = File("inputs/2019/18-2.txt").readLines()
        val mapInfo = scanMap(map)
        val startPoints = listOf('@', '@', '@', '@')
        println(measureTimedValue { minPathForAllKeysIn4Quadrants(mapInfo, startPoints) })
    }
}

@ExperimentalTime
fun main() {
    Day18.part1()
    Day18.part2()
}