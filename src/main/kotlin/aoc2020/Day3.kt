package aoc2020

import java.io.File


object Day3 {

    private fun List<String>.isTree(row: Int, col: Int): Boolean {
        val line = this[row]
        val char = line[col % line.length]
        return char == '#'
    }

    fun countTrees(map: List<String>, rowStep: Int, colStep: Int): Long {
        var row = rowStep
        var col = colStep
        var count = 0L
        while (row < map.size) {
            if (map.isTree(row, col)) {
                count++
            }
            row += rowStep
            col += colStep
        }
        return count
    }
}

fun main() {
    val map = File("inputs/2020/3.txt").readLines()
    val trees1 = Day3.countTrees(map, 1, 3)
    println(trees1)

    val product = trees1 *
            Day3.countTrees(map, 1, 1) *
            Day3.countTrees(map, 1, 5) *
            Day3.countTrees(map, 1, 7) *
            Day3.countTrees(map, 2, 1)
    println(product)
}