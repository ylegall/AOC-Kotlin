package aoc2016

import java.io.File

private const val WIDTH = 50
private const val HEIGHT = 6

private typealias Grid = Array<BooleanArray>

private sealed class Op {

    abstract fun apply(grid: Grid)

    data class RotateCol(val x: Int, val amount: Int): Op() {
        override fun apply(grid: Grid) {
            val rotation = amount % HEIGHT
            val newCol = (HEIGHT - rotation until (2*HEIGHT) - rotation).map { grid[it % HEIGHT][x] }
            for (row in 0 until HEIGHT) {
                grid[row][x] = newCol[row]
            }
        }
    }

    data class RotateRow(val y: Int, val amount: Int): Op() {
        override fun apply(grid: Grid) {
            val rotation = amount % WIDTH
            val newRow = (WIDTH - rotation until (2*WIDTH) - rotation).map { grid[y][it % WIDTH] }
            for (col in 0 until WIDTH) {
                grid[y][col] = newRow[col]
            }
        }
    }

    data class Rect(val width: Int, val height: Int): Op() {
        override fun apply(grid: Grid) {
            for (row in 0 until height) {
                for (col in 0 until width) {
                    grid[row][col] = true
                }
            }
        }
    }
}

private fun countLightsOnGrid(ops: Sequence<Op>): Int {
    return ops.fold(Array(HEIGHT) { BooleanArray(WIDTH) { false } }) { grid, op ->
        grid.also { op.apply(it) }.also { it.debug() }
    }.map { row ->
        row.count { it }
    }.sum()
}

private fun Grid.debug() {
    for (row in 0 until HEIGHT) {
        for (col in 0 until WIDTH) {
            print( if (this[row][col]) '#' else '.')
        }
        println()
    }
    println()
}

fun main() {
    File("inputs/2016/8.txt").useLines { lines ->
        val ops = lines.asSequence().map {
            it.split(" ", "x=", "y=", "x")
        }.map {
            when {
                it.size == 3 -> Op.Rect(it[1].toInt(), it[2].toInt())
                it[1] == "column" -> Op.RotateCol(it[3].toInt(), it[5].toInt())
                it[1] == "row" -> Op.RotateRow(it[3].toInt(), it[5].toInt())
                else -> throw IllegalStateException("Invalid operation")
            }
        }
        println(countLightsOnGrid(ops))
    }

}