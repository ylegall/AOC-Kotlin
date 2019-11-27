package aoc2016

import util.Point
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day22 {

    private val nodeRegex = Regex("""/dev/grid/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T""")

    private data class Node(
            val x: Int,
            val y: Int,
            val size: Int,
            val used: Int
    ) {
        fun availableSpace() = size - used

        fun isNotEmpty() = used > 0
    }

    private fun countViableNodes(nodeSequence: List<Node>): Int {
        val nodes = nodeSequence.toList()
        return nodes.indices.filter { nodes[it].isNotEmpty() }.flatMap { i ->
            nodes.indices.filter { it != i }.map { j ->
                val first = nodes[i]
                val second = nodes[j]
                first.used <= second.availableSpace()
            }
        }.count { it }
    }

    private fun parseInput(line: String): Node {
        val (x, y, size, used) = nodeRegex.find(line)?.destructured!!
        return Node(x.toInt(), y.toInt(), size.toInt(), used.toInt())
    }

    @ExperimentalTime
    fun run() {
        val nodes = File("inputs/2016/22.txt").readLines().drop(2).map { parseInput(it) }
        println(measureTimedValue { countViableNodes(nodes) })
        printGrid(nodes.toList())
    }

    fun printGrid(nodes: List<Node>) {
        val nodeMap = nodes.associateBy { Point(it.x, it.y) }
        val minSize = nodes.minBy { it.size }!!.size
        val maxRows = nodes.maxBy { it.y }!!.y
        val maxCols = nodes.maxBy { it.x }!!.x
        println("cols: $maxCols, rows: $maxRows")

        for (y in 0 .. maxRows) {
            for (x in 0 .. maxCols) {
                val char = nodeMap[Point(x, y)]?.let {
                    when {
                        x == 0 && y == 0 -> "T"
                        x == maxCols && y == 0 -> "G"
                        it.used > minSize -> "#"
                        it.used == 0 -> "_"
                        else -> "."
                    }
                } ?: " . "
                print(char)
            }
            println()
        }
    }
}

@ExperimentalTime
fun main() {
    Day22.run()
    // Part 2 - left as an exercise for the reader
}
