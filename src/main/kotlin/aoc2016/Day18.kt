package aoc2016

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day18  {

    private const val INITIAL_ROW = ".^^^.^.^^^^^..^^^..^..^..^^..^.^.^.^^.^^....^.^...^.^^.^^.^^..^^..^.^..^^^.^^...^...^^....^^.^^^^^^^"

    private fun String.tile(idx: Int) = if (idx in this.indices) this[idx] else '.'

    private fun String.nextRow(): String {
        return indices.map { nextTile(it) }.joinToString("")
    }

    private fun String.nextTile(idx: Int): Char {
        val left = tile(idx - 1)
        val center = tile(idx)
        val right = tile(idx + 1)
        return when {
            left == '^' && center == '^' && right == '.' -> '^'
            left == '.' && center == '^' && right == '^' -> '^'
            left == '^' && center == '.' && right == '.' -> '^'
            left == '.' && center == '.' && right == '^' -> '^'
            else                                         -> '.'
        }
    }

    private fun countSafeTilesInAllRows(times: Int): Int {
        var safeTiles = 0
        var row = INITIAL_ROW
        repeat(times) {
            safeTiles += row.count { it == '.' }
            row = row.nextRow()
        }
        return safeTiles
    }

    @ExperimentalTime
    fun run() {
        println(measureTimedValue { countSafeTilesInAllRows(400000) })
    }
}

@ExperimentalTime
fun main() {
    Day18.run()
}