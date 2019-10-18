package aoc2017

import util.Point
import util.knotHashToString


private const val INPUT = "amgozmfv"

private typealias Grid = List<MutableList<Int>>
private typealias ReadonlyGrid = List<List<Int>>

private fun Char.hexValue() = Integer.parseInt(toString(), 16)

private fun generateHashes(): List<String> {
    return (0 until 128).map { row ->
        val key = "$INPUT-$row".map { it.toInt() }
        knotHashToString(key)
    }
}

@ExperimentalStdlibApi
private fun countUsedSquares(): Int {
    return generateHashes().map { row ->
        row.map { it.hexValue().countOneBits() }.sum()
    }.sum()
}

private fun Int.toBitList() = MutableList(4) { idx ->
    val bitIdx = 1 shl (3 - idx)
    if (this and (bitIdx) != 0) -1 else 0
}

private fun generateBitsetGrid(): Grid {
    return generateHashes().map { row ->
        row.flatMap { it.hexValue().toBitList() }.toMutableList()
    }
}

private fun countConnectedGroups(): Int {
    val grid = generateBitsetGrid()
    var counter = 0
    for (row in grid.indices) {
        for (col in grid[row].indices) {
            if (grid[row][col] < 0) {
                counter++
                fillGrid(grid, Point(row, col), counter)
            }
        }
    }
    return counter
}

private fun ReadonlyGrid.getColor(p: Point): Int {
    return if (p.x in this.indices) {
        val row = this[p.x]
        if (p.y in row.indices) {
            row[p.y]
        } else {
            0
        }
    } else {
        0
    }
}

private fun fillGrid(grid: Grid, p: Point, color: Int) {
    grid[p.x][p.y] = color
    listOf(
            Point(p.x - 1, p.y),
            Point(p.x + 1, p.y),
            Point(p.x, p.y - 1),
            Point(p.x, p.y + 1)
    ).filter {
        grid.getColor(it) < 0
    }.forEach {
        fillGrid(grid, it, color)
    }
}

private fun ReadonlyGrid.print() {
    for (row in this) {
        println(row.joinToString("") { if (it == 0) "." else "#" })
    }
}

@ExperimentalStdlibApi
fun main() {
    println(countUsedSquares())
    println(countConnectedGroups())
}
