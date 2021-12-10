package aoc2021

import util.arrayDequeOf
import java.io.File

fun main() {

    fun height(coord: Pair<Int, Int>, grid: Array<IntArray>): Int {
        return grid[coord.first][coord.second]
    }

    fun neighbors(coord: Pair<Int, Int>, grid: Array<IntArray>): List<Pair<Int, Int>> {
        val (row, col) = coord
        return listOf(
            row - 1 to col,
            row to col - 1,
            row to col + 1,
            row + 1 to col,
        ).filter {
            it.first in grid.indices && it.second in grid[0].indices
        }
    }

    fun isLowPoint(coord: Pair<Int, Int>, grid: Array<IntArray>): Boolean {
        val currentHeight = height(coord, grid)
        return neighbors(coord, grid).all { height(it, grid) > currentHeight }
    }

    fun lowPoints(grid: Array<IntArray>): List<Pair<Int, Int>> {
        return grid.indices.asSequence().flatMap { row ->
            grid[row].indices.map { col -> row to col }.filter { coord ->
                isLowPoint(coord, grid)
            }
        }.toList()
    }

    fun part1(grid: Array<IntArray>): Int {
        return lowPoints(grid).sumOf { grid[it.first][it.second] + 1 }
    }

    fun part2(grid: Array<IntArray>): Int {
        val lowPoints = lowPoints(grid)
        val sizes = mutableListOf<Int>()
        for (i in lowPoints.indices) {
            val seen = mutableSetOf<Pair<Int, Int>>()
            val lowPoint = lowPoints[i]
            val q = arrayDequeOf(lowPoint)
            while (q.isNotEmpty()) {
                val pos = q.poll()
                if (pos !in seen) {
                    val nextPositions = neighbors(pos, grid).filter { nextPos ->
                        val nextValue = grid[nextPos.first][nextPos.second]
                        val currentValue = grid[pos.first][pos.second]
                        nextPos !in seen && nextValue < 9 && nextValue > currentValue
                    }
                    q.addAll(nextPositions)
                }
                seen.add(pos)
            }
            sizes.add(seen.size)
        }
        return sizes.sortedDescending().take(3).reduce { a, b -> a * b }
    }

    val input = File("inputs/2021/input.txt").readLines()
    val grid = Array(input.size) { row ->
        IntArray(input[row].length) { col -> input[row][col].digitToInt() }
    }

    println(part1(grid))
    println(part2(grid))
}