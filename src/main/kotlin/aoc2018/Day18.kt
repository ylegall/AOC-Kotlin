package aoc2018

import util.input
import kotlin.streams.toList

private object Day18 {

    private const val EMPTY  = '.'
    private const val TREES  = '|'
    private const val LUMBER = '#'

    private class Grid(initialState: List<CharArray>) {
        private val grids = arrayOf(ArrayList<CharArray>(), ArrayList())
        private var gridPointer = 0

        init {
            for (row in initialState) {
                grids[0].add(row)
                grids[1].add(row.copyOf(row.size))
            }
        }

        private fun neighbors(rowIndex: Int, colIndex: Int): List<Char> {
            val grid = grids[gridPointer]
            val row = grid[rowIndex]
            val prevRow = grid.getOrNull(rowIndex - 1)
            val nextRow = grid.getOrNull(rowIndex + 1)
            return listOfNotNull(
                    prevRow?.getOrNull(colIndex - 1),
                    prevRow?.getOrNull(colIndex),
                    prevRow?.getOrNull(colIndex + 1),
                    row.getOrNull(colIndex - 1),
                    row.getOrNull(colIndex + 1),
                    nextRow?.getOrNull(colIndex - 1),
                    nextRow?.getOrNull(colIndex),
                    nextRow?.getOrNull(colIndex + 1)
            )
        }

        fun next() {
            val grid = grids[gridPointer]
            val tempGrid = grids[(gridPointer + 1) % grids.size]
            for (row in grid.indices) {
                for (col in grid[row].indices) {
                    val neighbors = neighbors(row, col)
                    when (grid[row][col]) {
                        EMPTY -> {
                            tempGrid[row][col] = if (neighbors.count { it == TREES } >= 3) {
                                TREES
                            } else {
                                EMPTY
                            }
                        }
                        TREES -> {
                            tempGrid[row][col] = if (neighbors.count { it == LUMBER } >= 3) {
                                LUMBER
                            } else {
                                TREES
                            }
                        }
                        LUMBER -> {
                            tempGrid[row][col] = if (
                                    neighbors.count { it == LUMBER } >= 1 &&
                                    neighbors.count { it == TREES } >= 1
                            ) {
                                LUMBER
                            } else {
                                EMPTY
                            }
                        }
                    }
                }
            }
            gridPointer = ((gridPointer + 1) % grids.size)
        }

        fun cellCounts(): Map<Char, Int> {
            return grids[gridPointer].flatMap {
                it.toList()
            }.groupingBy {
                it
            }.eachCount()
        }

        fun print() {
            for (row in grids[gridPointer]) {
                println(row.joinToString(""))
            }
        }
    }

    private fun valueAfterSteps(steps: Int) {
        val grid = Grid(parseInput())
        repeat(steps) { step ->
            grid.next()
            //grid.print()
            val counts = grid.cellCounts()
            val score = counts.getOrDefault(TREES, 0) * counts.getOrDefault(LUMBER, 0)
            println("${step + 1}, ${(step + 1) % 28}: $score")
        }
    }

    private fun parseInput() = input("inputs/2018/18.txt").use { it.map { line -> line.toCharArray() } }.toList()

    fun run() {
        println(valueAfterSteps(1000))
    }
}

fun main() {
    Day18.run()
}