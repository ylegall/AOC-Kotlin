package aoc2021

import util.Point
import java.io.File


fun main() {


    fun List<CharArray>.print() {
        val text = this.joinToString("\n") {
            it.joinToString("")
        }
        println(text)
        println()
    }

    fun doStep(grid: List<CharArray>): Boolean {
        var changed = false
        val moves = mutableListOf<Pair<Point, Point>>()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == '>') {
                    val nextCol = (col + 1) % grid[row].size
                    if (grid[row][nextCol] == '.') {
                        moves.add(Point(row, col) to Point(row, nextCol))
                        changed = true
                    }
                }
            }
        }

        for (move in moves) {
            val (p1, p2) = move
            grid[p1.x][p1.y] = '.'
            grid[p2.x][p2.y] = '>'
        }
        moves.clear()

        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == 'v') {
                    val nextRow = (row + 1) % grid.size
                    if (grid[nextRow][col] == '.') {
                        moves.add(Point(row, col) to Point(nextRow, col))
                        changed = true
                    }
                }
            }
        }
        for (move in moves) {
            val (p1, p2) = move
            grid[p1.x][p1.y] = '.'
            grid[p2.x][p2.y] = 'v'
        }

        return changed
    }

    fun simulateSteps(grid: List<CharArray>): Long {

//        println("initial state:")
//        grid.print()

        var steps = 1L
        while (true) {
            if (!doStep(grid)) {
                break
            }
            steps++

//            println("after $steps steps:")
//            grid.print()

            if (steps == 10000L) {
                break
            }
        }
        return steps
    }

    val grid = File("inputs/2021/input.txt").useLines { lines ->
        lines.map { it.toCharArray() }.toList()
    }

    println(simulateSteps(grid))
}