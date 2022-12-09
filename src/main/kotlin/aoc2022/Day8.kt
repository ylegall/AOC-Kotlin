package aoc2022

import java.io.File
import kotlin.math.max


typealias Coord = Pair<Int, Int>

fun main() {

    data class Tree(
        val coord: Coord,
        val height: Int
    )

    val input = File("input.txt").readLines().mapIndexed { row, line ->
        line.mapIndexed { col, h -> Tree(row to col, h.digitToInt()) }
    }

    fun List<List<Tree>>.getCol(col: Int) = this.indices.map { row -> this[row][col] }

    fun visibleAlongRow(trees: List<Tree>): List<Tree> {
        var maxHeight = -1
        return trees.mapNotNull { tree ->
            tree.takeIf { it.height > maxHeight }.also { maxHeight = max(maxHeight, tree.height) }
        }
    }

    fun visibleFromTree(trees: List<Tree>): List<Pair<Tree, Int>> {
        val recentlySeen = Array(10) { 0 }
        return trees.mapIndexed { i, tree ->
            val tallerIndex = recentlySeen.slice(tree.height .. 9).maxOrNull() ?: 0
            val shorterCount = i - tallerIndex
            recentlySeen[tree.height] = i
            tree to shorterCount
        }
    }

    fun part1() {
        val visibleTrees = input.flatMap { row -> visibleAlongRow(row) }.union(
            input.flatMap { row -> visibleAlongRow(row.reversed()) }
        ).union(
            input.indices.flatMap { c -> visibleAlongRow(input.getCol(c)) }
        ).union(
            input.indices.flatMap { c -> visibleAlongRow(input.getCol(c).reversed()) }
        )

        println(visibleTrees.size)
    }

    fun part2() {
        val bestScore = input.flatMap { row -> visibleFromTree(row) }.union(
            input.flatMap { row -> visibleFromTree(row.reversed()) }
        ).union(
            input.indices.flatMap { c -> visibleFromTree(input.getCol(c)) }
        ).union(
            input.indices.flatMap { c -> visibleFromTree(input.getCol(c).reversed()) }
        ).groupBy { (tree, _) ->
            tree
        }.mapValues { entry ->
            entry.value.map { (_, count) -> count }.reduce { a, b -> a * b }
        }.maxBy { it.value }

        println(bestScore.value)
    }

    part1()
    part2()

}