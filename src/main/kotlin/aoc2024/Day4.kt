package aoc2024

import java.io.File

fun main() {

    val target = "XMAS"

    val input = File("input.txt").readLines()

    val directions = (-1 .. 1).flatMap { rowDir ->
        (-1 .. 1).map { colDir -> rowDir to colDir }
    }

    fun findDirection(row: Int, col: Int, dir: Pair<Int, Int>): Boolean {
        var i = 0
        var r = row
        var c = col
        while (i < target.length) {
            if (r < 0 || r >= input.size) return false
            if (c < 0 || c >= input[r].length) return false
            if (input[r][c] != target[i]) return false
            i++
            r += dir.first
            c += dir.second
        }
        return true
    }

    fun findAtPosition(row: Int, col: Int): List<Pair<Int, Int>> {
        return directions.filter { dir -> findDirection(row, col, dir) }
    }

    fun part1() {
        var sum = 0
        for (r in input.indices) {
            for (c in input[r].indices) {
                sum += findAtPosition(r, c).size
            }
        }
        println(sum)
    }

    fun isCross(row: Int, col: Int): Boolean {
        if (input[row][col] != 'A') return false
        if (row <= 0 || row >= input.size-1) return false
        if (col <= 0 || col >= input[row].length-1) return false
        val upLeft = input[row-1][col-1]
        val upRight = input[row-1][col+1]
        val downLeft = input[row+1][col-1]
        val downRight = input[row+1][col+1]
        return (
                (upLeft == 'M' && downRight == 'S') ||
                (upLeft == 'S' && downRight == 'M')
            ) && (
                (downLeft == 'M' && upRight == 'S') ||
                (downLeft == 'S' && upRight == 'M')
            )
    }

    fun part2() {
        val count = input.indices.sumOf { row ->
            input[row].indices.count { col -> isCross(row, col) }
        }
        println(count)
    }

    part1()

    part2()
}