package aoc2019

import util.Point
import util.findPoint
import java.io.File

private object Day18_Test {

    private val maze = File("inputs/2019/18.txt").readLines()
    private val seenPoints = HashSet<Point>()

    private class SearchState(
            val pos: Point,
            val steps: Int = 0
    )

    private data class MazeData(
            val keys: Set<Char> = emptySet(),
            val doors: Map<Char, MazeData?> = emptyMap()
    ) {
        fun addKey(key: Char) = MazeData(keys + key, doors)

        fun addDoor(door: Char) = MazeData(emptySet(), mapOf(door to this))

        fun print(tabLevel: Int = 0) {
            if (keys.isNotEmpty()) {
                println(" ".repeat(tabLevel) + "keys: $keys")
            }
            for ((door, data) in doors) {
                println(" ".repeat(tabLevel) + door)
                data?.print(tabLevel + 2)
            }
        }
    }

    private fun combineMazeData(data: List<MazeData>): MazeData {
        val keys = data.flatMap { it.keys }.toSet()
        val doors = data.flatMap { it.doors.entries }.associate { it.toPair() }
        return MazeData(keys, doors)
    }

    private fun scanMaze(state: SearchState): MazeData {
        seenPoints.add(state.pos)
        val openNeighbors = state.pos.cardinalNeighbors().filter {
            maze[it.y][it.x] != '#' && it !in seenPoints
        }

        val tileCharacter = maze[state.pos.y][state.pos.x]

        fun MazeData.addTileToResults(tile: Char) = when (tile) {
            in 'a'..'z' -> addKey(tile)
            in 'A'..'Z' -> addDoor(tile)
            else -> this
        }

        return when (openNeighbors.size) {
            0 -> MazeData().addTileToResults(tileCharacter)
            1 -> {
                val otherData = scanMaze(SearchState(openNeighbors.first(), state.steps + 1))
                otherData.addTileToResults(tileCharacter)
            }
            else -> {
                val data = openNeighbors.map { nextPosition ->
                    scanMaze(SearchState(nextPosition, state.steps + 1))
                }
                combineMazeData(data).addTileToResults(tileCharacter)
            }
        }
    }

    fun run() {
        val startPoint = maze.map { it.toList() }.findPoint { it == '@' }!!
        println(startPoint)
        val allData = scanMaze(SearchState(startPoint))
        println("found all data")
        allData.print()
    }
}

fun main() {
    Day18_Test.run()
}