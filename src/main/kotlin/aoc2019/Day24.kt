package aoc2019

import util.Point
import util.get
import util.getOrNull
import java.io.File


private object Day24 {

    private val grid = File("inputs/2019/24.txt").readLines()

    private class SimpleCells(
            private var cells: List<String>
    ) {
        fun step() {
            cells = (cells.indices).map { y ->
                cells[y].indices.map { x ->
                    val point = Point(x, y)
                    val cell = cells[point]
                    val neighbors = neighbors(point)
                    when (cell) {
                        '#' -> if (neighbors != 1) '.' else '#'
                        '.' -> if (neighbors in 1 .. 2) '#' else '.'
                        else -> throw Exception("bad cell: $cell")
                    }
                }.joinToString("")
            }
        }

        private fun neighbors(point: Point) = point.cardinalNeighbors().count { cells.getOrNull(it) == '#' }

        fun biodiversity() = cells.flatMap { row ->
            row.toList()
        }.mapIndexed { index, char ->
            if (char == '#') (1 shl index) else 0
        }.reduce { a, b ->
            a or b
        }
    }

    fun firstRepeatedBiodiversity() {
        val cells = SimpleCells(grid)
        val seen = hashSetOf(cells.biodiversity())
        var steps = 0
        while (true) {
            cells.step()
            steps++
            val biodiversity = cells.biodiversity()
            if (biodiversity in seen) {
                println("found repeat biodiversity: $biodiversity at step $steps")
                break
            } else {
                seen.add(cells.biodiversity())
            }
        }
    }

    private data class Coord(
            val x: Int,
            val y: Int,
            val level: Int
    ) {
        constructor(point: Point, level: Int = 0): this(point.x, point.y, level)

        val point: Point; get() = Point(x, y)
    }

    private class RecursiveCells(
            input: List<String>
    ) {
        private val height = input.size
        private val width = input[0].length
        private var cells = mutableSetOf<Coord>()

        val size: Int; get() = cells.size

        init {
            for (row in input.indices) {
                for (col in input[row].indices) {
                    if (input[row, col] == '#') {
                        cells.add(Coord(row, col, 0))
                    }
                }
            }
        }

        fun Coord.livingNeighbors() = neighbors(this).count { it in cells }

        fun step() {
            cells = cells.flatMap { cell ->
                val deadNeighbors = neighbors(cell).filter { it !in cells }
                val newCells = deadNeighbors.filter { neighbor ->
                    neighbor.livingNeighbors() in 1..2
                }

                if (cell.livingNeighbors() == 1) {
                    newCells + cell
                } else {
                    newCells
                }
            }.toMutableSet()
        }

        private fun neighbors(coord: Coord): List<Coord> {
            val point = coord.point
            return point.cardinalNeighbors().flatMap {
                when {
                    it.x >= height -> listOf(Coord(3, 2, coord.level - 1))
                    it.y >= width  -> listOf(Coord(2, 3, coord.level - 1))
                    it.x < 0       -> listOf(Coord(1, 2, coord.level - 1))
                    it.y < 0       -> listOf(Coord(2, 1, coord.level - 1))
                    it.x == 2 && it.y == 2 -> when {
                        point.x > it.x -> (0 until width).map { col -> Coord(4, col, coord.level + 1) }
                        point.y > it.y -> (0 until height).map { row -> Coord(row, 4, coord.level + 1) }
                        point.x < it.x -> (0 until width).map { col -> Coord(0, col, coord.level + 1) }
                        point.y < it.y -> (0 until height).map { row -> Coord(row, 0, coord.level + 1) }
                        else           -> throw Exception("bad coord: $point")
                    }
                    else -> listOf(Coord(it, coord.level))
                }
            }
        }
    }

    fun cellsAfter200Iterations() {
        val cells = RecursiveCells(grid)
        repeat(200) {
            cells.step()
        }
        println(cells.size)
    }
}

// 29359514 too high
fun main() {
    Day24.firstRepeatedBiodiversity()
    Day24.cellsAfter200Iterations()
}