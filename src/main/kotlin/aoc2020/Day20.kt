package aoc2020

import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.math.sqrt


private object Day20 {

    enum class Flip { FRONT, BACK }

    private val encoding = StandardCharsets.UTF_8.name()

    data class Orientation(val rotations: Int, val flip: Flip) {
        fun flip() = when (flip) {
            Flip.FRONT -> Orientation(rotations, Flip.BACK)
            Flip.BACK  -> Orientation(rotations, Flip.FRONT)
        }

        fun rotate() = Orientation((rotations + 1) % 4, flip)
    }

    data class Tile(
            val id: String,
            //val data: List<String>,
            val edges: List<String>,
            val orientation: Orientation = Orientation(0, Flip.FRONT),
    ) {
        fun flip(): Tile {
            val flippedEdges = listOf(
                    edges[2],
                    edges[1].reversed(),
                    edges[0],
                    edges[3].reversed(),
            )
            return Tile(id, flippedEdges, orientation.flip())
        }

        fun rotate(): Tile {
            val rotatedEdges = listOf(
                    edges[3].reversed(),
                    edges[0],
                    edges[1].reversed(),
                    edges[2]
            )
            return Tile(id, rotatedEdges, orientation.rotate())
        }

        fun expandStates(): Set<Tile> {
            var tile = this
            val orientations = mutableSetOf(tile)
            repeat(3) {
                tile = tile.rotate()
                orientations.add(tile)
            }
            tile = this.flip()
            orientations.add(tile)
            repeat(3) {
                tile = tile.rotate()
                orientations.add(tile)
            }
            return orientations
        }

        override fun toString(): String {
            //return "Tile $id\n${data.joinToString("\n")}\n"
            return "Tile $id\n$orientation\n$edges\n\n"
        }
    }

    private val defaultOrientation = Orientation(0, Flip.FRONT)
    private val emptyTile = Tile("", emptyList(), defaultOrientation)

    fun tile(id: String, data: List<String>, orientation: Orientation = defaultOrientation): Tile {
        val edges = listOf(
                data.first(),
                data.map { it.last() }.joinToString(""),
                data.last(),
                data.map { it.first() }.joinToString(""),
        )
        return Tile(id, edges, orientation)
    }

    fun buildTileMap(tiles: List<Tile>): Map<Pair<String, Int>, Set<Tile>> {
        val edgeMap = mutableMapOf<Pair<String, Int>, MutableSet<Tile>>()
        for (tile in tiles) {
            tile.edges.forEachIndexed { edgeIndex, edge ->
                val validTiles = edgeMap.getOrPut(edge to edgeIndex) { mutableSetOf() }
                validTiles.add(tile)
            }
        }
        return edgeMap
    }

    fun parseInput(lines: List<String>): List<Tile> {
        val tiles = mutableListOf<Tile>()
        val tileData = mutableListOf<String>()
        var id = ""
        for (line in lines) {
            when {
                line.startsWith("Tile") -> {
                    val parts = line.split(" ", ":")
                    id = parts[1]
                }
                line.isEmpty() && tileData.isNotEmpty() -> {
                    tiles.add(tile(id, tileData.toList()))
                    tileData.clear()
                }
                else -> {
                    tileData.add(line)
                }
            }
        }
        if (tileData.isNotEmpty()) {
            tiles.add(tile(id, tileData.toList()))
        }
        return tiles
    }

    private class SearchState(
            val row: Int,
            val col: Int,
            val grid: List<MutableList<Tile>>,
            val edgeMap: Map<Pair<String, Int>, Set<Tile>>,
            val allTiles: Set<Tile>,
            val usedTiles: MutableSet<String>
    ) {
        fun next(): SearchState {
            var nextRow = row
            var nextCol = col + 1
            if (nextCol == grid[row].size) {
                nextCol = 0
                nextRow++
            }
            return SearchState(nextRow, nextCol, grid, edgeMap, allTiles, usedTiles)
        }
    }

    private fun fillBoard(state: SearchState): Boolean {
        val row = state.row
        val col = state.col

        //println("($row, $col)")
        //println(state.grid.joinToString("\n") { it.joinToString(" ") { it.id } })
        //println()

        if (row >= state.grid.size) {
            return true
        }
        val topEdge = state.grid.getOrNull(state.row - 1)?.get(state.col)?.edges?.get(2)
        val leftEdge = state.grid[state.row].getOrNull(state.col - 1)?.edges?.get(1)
        val topCandidates = topEdge?.let { state.edgeMap[it to 0] } ?: state.allTiles
        val leftCandidates = leftEdge?.let { state.edgeMap[it to 3] } ?: state.allTiles
        val allCandidates = (topCandidates.intersect(leftCandidates)).filter { it.id !in state.usedTiles }
        for (candidate in allCandidates) {
            state.grid[row][col] = candidate
            state.usedTiles.add(candidate.id)
            val goalFound = fillBoard(state.next())
            if (goalFound) {
                return true
            }
            state.grid[row][col] = emptyTile
            state.usedTiles.remove(candidate.id)
        }
        return false
    }

    fun fillBoard(tiles: List<Tile>) {
        val maxRows = sqrt(tiles.size.toDouble()).toInt()
        val maxCols = maxRows
        println("$maxRows x $maxCols")
        val grid = List(maxRows) { MutableList(maxCols) { emptyTile } }
        val expandedTiles = tiles.flatMap { it.expandStates() }
        val edgeMap = buildTileMap(expandedTiles)
        val initialState = SearchState(0, 0, grid, edgeMap, expandedTiles.toSet(), mutableSetOf())

        val result = fillBoard(initialState)

        if (!result) {
            println("failed to fill board")
        } else {
            val topLeft = initialState.grid.first().first().id.toLong()
            val topRight = initialState.grid.first().last().id.toLong()
            val bottomLeft = initialState.grid.last().first().id.toLong()
            val bottomRight = initialState.grid.last().last().id.toLong()
            println("$topLeft $topRight")
            println("$bottomLeft $bottomRight")
            //println(initialState.grid.joinToString("\n"))
            println(topLeft * topRight * bottomLeft * bottomRight)

            println(grid.joinToString("\n") { it.joinToString { it.id } })
        }
    }


}

fun main() {
    val input = File("inputs/2020/20.txt").readLines()
    val tiles = Day20.parseInput(input)

    Day20.fillBoard(tiles)
}