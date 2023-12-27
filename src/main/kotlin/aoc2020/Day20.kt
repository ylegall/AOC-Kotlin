package aoc2020

import util.rotate
import util.rotateInPlace
import java.io.File
import kotlin.math.sqrt


private object Day20 {

    enum class Flip { FRONT, BACK }

    data class Orientation(val rotations: Int, val flip: Flip) {
        fun flip() = when (flip) {
            Flip.FRONT -> Orientation(rotations, Flip.BACK)
            Flip.BACK  -> Orientation(rotations, Flip.FRONT)
        }

        fun rotate() = Orientation((rotations + 1) % 4, flip)
    }

    data class Tile(
            val id: String,
            val data: List<String>,
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
            return Tile(id, data.reversed(), flippedEdges, orientation.flip())
        }

        fun rotate(): Tile {
            val rotatedEdges = listOf(
                    edges[3].reversed(),
                    edges[0],
                    edges[1].reversed(),
                    edges[2]
            )
            return Tile(id, data.rotate(), rotatedEdges, orientation.rotate())
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
            return "Tile $id\n$orientation\n$edges\n\n"
        }
    }

    private val defaultOrientation = Orientation(0, Flip.FRONT)
    private val emptyTile = Tile("", emptyList(), emptyList(), defaultOrientation)

    fun tile(id: String, data: List<String>, orientation: Orientation = defaultOrientation): Tile {
        val edges = listOf(
                data.first(),
                data.map { it.last() }.joinToString(""),
                data.last(),
                data.map { it.first() }.joinToString(""),
        )
        return Tile(id, data, edges, orientation)
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

    fun fillBoard(tiles: List<Tile>): List<List<Tile>> {
        val maxRows = sqrt(tiles.size.toDouble()).toInt()
        val maxCols = maxRows
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
            println(topLeft * topRight * bottomLeft * bottomRight)
            // println(grid.joinToString("\n") { it.joinToString { "${it.id} ${it.orientation}" } })
        }
        return grid
    }

    fun findImage(grid: List<List<Tile>>) {
        val pattern = listOf(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
        )

        val tileRows = grid.first().first().data.size
        val tileCols = grid.first().first().data.first().length

        val trimmedGrid = mutableListOf<MutableList<Char>>()
        for (row in grid.indices) {
            for (subRow in 1 until tileRows-1) {
                val trimmedRow = grid[row].joinToString("") { tile ->
                    tile.data[subRow].slice(1 until tileCols-1)
                }
                trimmedGrid.add(trimmedRow.toMutableList())
            }
        }

        fun matchPattern(startRow: Int, startCol: Int, data: List<List<Char>>): Int {
            var count = 0
            for (r in pattern.indices) {
                for (c in pattern[r].indices) {
                    val patternTile = pattern[r][c]
                    if (patternTile == '#') {
                        val dataTile = data[startRow + r][startCol + c]
                        if (dataTile == '#') {
                            count += 1
                        } else {
                            return 0
                        }
                    }
                }
            }
            return count
        }

        fun orientAndFindMatches(data: MutableList<MutableList<Char>>): Int {
            val candidates = data.sumOf { line ->
                line.count { it == '#' }
            }.also { println("candidate tiles: $it") }

            repeat(2) {
                repeat(4) {
                    var totoalMonsterTiles = 0
                    for (startRow in 0 until data.size - pattern.size + 1) {
                        for (startCol in 0 until data[startRow].size - pattern.first().length + 1) {
                            totoalMonsterTiles += matchPattern(startRow, startCol, data)
                        }
                    }
                    if (totoalMonsterTiles > 0) {
                        return candidates - totoalMonsterTiles
                    }
                    data.rotateInPlace()
                }
                data.reverse()
            }
            println("no matches found")
            return -1
        }

        val result = orientAndFindMatches(trimmedGrid)
        println(result)
    }

}

fun main() {
    val input = File("input.txt").readLines()
    val tiles = Day20.parseInput(input)
    val grid = Day20.fillBoard(tiles)
    Day20.findImage(grid)
}