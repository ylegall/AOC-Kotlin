package aoc2015

import util.input
import kotlin.streams.asSequence

private typealias Grid = List<List<Boolean>>
private const val LEN = 100

private fun Grid.activeNeighbors(row: Int, col: Int): Int {
    return (row-1 .. row+1).filter { it in 0 until LEN }.map { y ->
        (col-1 .. col+1).filter { it in 0 until LEN }.count { x ->
            this[y][x]
        }
    }.sum() - if (this[row][col]) 1 else 0
}

private fun Grid.nextState(row: Int, col: Int): Boolean {
    val isOn = this[row][col]
    val neighbors = activeNeighbors(row, col)
    return if (isOn) {
        neighbors in (2 .. 3)
    } else {
        neighbors == 3
    }
}

private fun Grid.nextState(): Grid {
    val newGrid = List(LEN) { MutableList(LEN) { false }}
    for (row in 0 until LEN) {
        for (col in 0 until LEN) {
            newGrid[row][col] = nextState(row, col)
        }
    }
    return newGrid
}

private fun Grid.nextStateWithCornersOn(): Grid {
    val newGrid = List(LEN) { MutableList(LEN) { false }}
    for (row in 0 until LEN) {
        for (col in 0 until LEN) {
            newGrid[row][col] = nextState(row, col)
        }
    }
    newGrid[0][0] = true
    newGrid[0][LEN-1] = true
    newGrid[LEN-1][0] = true
    newGrid[LEN-1][LEN-1] = true
    return newGrid
}

private fun countLights1(startGrid: Grid): Int {
    var grid = startGrid
    for (i in 1 .. 100) grid = grid.nextState()
    return grid.map { row -> row.count { it } }.sum()
}

private fun countLights2(startGrid: Grid): Int {
    var grid = startGrid
    for (i in 1 .. 100) grid = grid.nextStateWithCornersOn()
    return grid.map { row -> row.count { it } }.sum()
}

fun main() {
    val grid = input("inputs/2015/18.txt").use { lines ->
        lines.asSequence().map { line ->
            line.map { it != '.' }
        }.toList()
    }
    println(countLights1(grid))
    println(countLights2(grid))
}