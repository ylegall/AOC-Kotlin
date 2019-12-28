package aoc2019

import util.Point
import util.get
import java.awt.Label
import java.io.File
import java.util.ArrayDeque

private object Day20 {

    private val maze = File("inputs/2019/20.txt").readLines()
    private var start = Point(0, 0)
    private var stop = Point(0, 0)
    private val portals = scanMaze()

    private fun scanMaze(): Map<Point, Point> {
        val labels = mutableMapOf<String, MutableList<Point>>()

        fun appendLabel(label: String, x: Int, y: Int) {
            labels.getOrPut(label) { mutableListOf() }.add(Point(x, y))
        }

        fun checkForLabel(x: Int, y: Int) {
            val top1 = maze[y-2, x]
            val top2 = maze[y-1, x]
            if (top1.isLetter() and top2.isLetter()) {
                appendLabel("$top1$top2", x, y)
                return
            }
            val bottom1 = maze[y+1, x]
            val bottom2 = maze[y+2, x]
            if (bottom1.isLetter() and bottom2.isLetter()) {
                appendLabel("$bottom1$bottom2", x, y)
                return
            }
            val right1 = maze[y, x+1]
            val right2 = maze[y, x+2]
            if (right1.isLetter() and right2.isLetter()) {
                appendLabel("$right1$right2", x, y)
                return
            }
            val left1 = maze[y, x-2]
            val left2 = maze[y, x-1]
            if (left1.isLetter() and left2.isLetter()) {
                appendLabel("$left1$left2", x, y)
                return
            }
        }

        for (y in 2 until maze.size - 2) {
            val row = maze[y]
            for (x in 2 until row.length - 2) {
                if (maze[y][x] != '.') continue
                checkForLabel(x, y)
            }
        }

        start = labels["AA"]!!.first()
        stop = labels["ZZ"]!!.first()

        val portals = labels.entries.filter {
            it.key != "ZZ" && it.key != "AA"
        }
        portals.forEach { (k, v) ->
            check(v.size == 2) { "invalid entry: $k:$v" }
        }
        return portals.flatMap { it.value.let { listOf(it.first() to it.last(), it.last() to it.first()) }}.toMap()
    }

    private fun shortestPath(): Int {
        val seen = HashSet<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(start to 0)
        while (queue.isNotEmpty()) {
            val (pos, steps) = queue.poll()
            if (pos == stop) {
                return steps
            }
            seen.add(pos)
            val neighbors = pos.cardinalNeighbors().filter {
                maze[it.y][it.x] == '.' && it !in seen
            }.let {
                if (pos in portals) it + portals[pos]!! else it
            }
            queue.addAll(neighbors.map { it to steps + 1 })
        }
        throw Exception("goal not found")
    }

    fun run() {
        println(shortestPath())
    }
}

fun main() {
    Day20.run()
}