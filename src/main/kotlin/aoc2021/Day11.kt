
package aoc2021

import java.io.File
import kotlin.math.max
import kotlin.math.min


fun main() {

    fun flash(row: Int, col: Int, board: List<IntArray>) {
        for (r in max(0, row - 1) .. min(board.size - 1, row + 1)) {
            for (c in max(0, col - 1) .. min(board[r].size - 1, col + 1)) {
                if (board[r][c] > 0) {
                    board[r][c] += 1
                }
            }
        }
    }

    fun step(grid: List<IntArray>): Int {
        for (row in grid) {
            for (c in row.indices) {
                row[c] += 1
            }
        }

        var flashes = 0
        while (true) {
            var newFlashes = 0
            for (r in grid.indices) {
                for (c in grid[r].indices) {
                    if (grid[r][c] > 9) {
                        grid[r][c] = 0
                        flash(r, c, grid)
                        newFlashes += 1
                    }
                }
            }

            flashes += newFlashes
            if (newFlashes == 0) {
                break
            }
        }

        return flashes
    }

    fun part1(grid: List<IntArray>): Int {
        var totalFlashes = 0
        for (step in 0 until 100) {
            totalFlashes += step(grid)
        }
        return totalFlashes
    }

    fun part2(grid: List<IntArray>): Int {
        var step = 1
        while (true) {
            step(grid)
            if (grid.all { row -> row.all { it == 0 } }) {
                return step
            }
            step += 1
        }
    }

    fun loadInput() = File("inputs/2021/input.txt").useLines { lines ->
        lines.map { line ->
            line.map { it.digitToInt() }.toIntArray()
        }.toList()
    }

    println(part1(loadInput()))
    println(part2(loadInput()))
}