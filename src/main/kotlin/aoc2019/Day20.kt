package aoc2019

import util.Point
import java.io.File
import java.util.ArrayDeque
import java.util.PriorityQueue

private object Day20 {

    private val maze = File("inputs/2019/20.txt").readLines()

    private val topInteriorRow = maze.indices.drop(2).first { row -> maze[row].trim().contains(' ') } - 1
    private val bottomInteriorRow = maze.indices.reversed().drop(2).first { row -> maze[row].trim().contains(' ') } + 1
    private val leftInteriorCol = maze[topInteriorRow + 1].trim().indexOf(' ') + 2 - 1
    private val rightInteriorCol = maze[topInteriorRow + 1].trim().lastIndexOf(' ') + 2 + 1

    private var start = Point(0, 0)
    private var stop = Point(0, 0)
    private val portalPairs = findPortals()
    private val portalPoints = portalPairs.entries.associate { it.key.pos to it.key }

    private data class Portal(
            val label: String,
            val pos: Point,
            val isInterior: Boolean
    )

    private fun findPortals(): Map<Portal, Portal> {
        val portalPairs = mutableMapOf<String, MutableList<Portal>>()
        val lastRow = maze.size - 3
        val lastCol = maze[0].length - 3

        fun addPortals(points: Iterable<Pair<Point, String>>, interior: Boolean = false) = points.filter {
            it.second[0].isLetter()
        }.forEach { (pos, label) ->
            val portal = Portal(label, pos, interior)
            portalPairs.getOrPut(label) { mutableListOf() }.add(portal)
        }

        // top exterior
        addPortals(maze[2].indices.map { col ->
            Point(col, 2) to "${maze[0][col]}${maze[1][col]}"
        })
        // bottom exterior
        addPortals(maze[lastRow].indices.map { col ->
            Point(col, lastRow) to "${maze[lastRow + 1][col]}${maze[lastRow + 2][col]}"
        })
        // left exterior
        addPortals(maze.indices.map { row ->
            Point(2, row) to "${maze[row][0]}${maze[row][1]}"
        })
        // right exterior
        addPortals(maze.indices.map { row ->
            Point(lastCol, row) to "${maze[row][lastCol + 1]}${maze[row][lastCol + 2]}"
        })

        // top interior
        addPortals(
                maze[topInteriorRow].indices.map { col ->
                    Point(col, topInteriorRow) to "${maze[topInteriorRow + 1][col]}${maze[topInteriorRow + 2][col]}"
                },
                interior = true
        )

        // bottom interior
        addPortals(
                maze[bottomInteriorRow].indices.map { col ->
                    Point(col, bottomInteriorRow) to "${maze[bottomInteriorRow - 2][col]}${maze[bottomInteriorRow - 1][col]}"
                },
                interior = true
        )
        // left interior
        addPortals(
                (topInteriorRow..bottomInteriorRow).map { row ->
                    Point(leftInteriorCol, row) to "${maze[row][leftInteriorCol + 1]}${maze[row][leftInteriorCol + 2]}"
                },
                interior = true
        )
        // right interior
        addPortals(
                (topInteriorRow..bottomInteriorRow).map { row ->
                    Point(rightInteriorCol, row) to "${maze[row][rightInteriorCol - 2]}${maze[row][rightInteriorCol - 1]}"
                },
                interior = true
        )

        start = portalPairs["AA"]!![0].pos
        stop = portalPairs["ZZ"]!![0].pos

        return portalPairs.entries.filter {
            it.value.size == 2
        }.flatMap {
            listOf(it.value[0] to it.value[1], it.value[1] to it.value[0])
        }.toMap()
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

            val nextPortalPosition = portalPoints[pos]?.let {
                portalPairs[it]
            }?.pos?.takeIf { it !in seen }

            val nextPoints = if (nextPortalPosition != null) {
                listOf(nextPortalPosition to steps + 1)
            } else {
                pos.cardinalNeighbors().filter {
                    maze[it.y][it.x] == '.' && it !in seen
                }.map { it to steps + 1 }
            }

            queue.addAll(nextPoints)
        }
        throw Exception("goal not found")
    }

    private data class SearchState(
            val pos: Point,
            val steps: Int = 0,
            val depth: Int = 0
    )

    private fun shortestPathRecursive(): Int {
        val seen = HashSet<Pair<Point, Int>>()
        val queue = PriorityQueue<SearchState>( compareBy({ it.steps }, { -it.depth }) )
        queue.add(SearchState(start))

        while (queue.isNotEmpty()) {
            val (pos, steps, depth) = queue.poll()
            if (pos == stop && depth == 0) {
                return steps
            }
            seen.add(pos to depth)

            val nextHop = if (pos in portalPoints) {
                val portal = portalPoints[pos]!!
                val nextPos = portalPairs[portal]!!.pos
                val nextDepth = if (portal.isInterior) depth + 1 else depth - 1
                if ((nextPos to nextDepth) !in seen) {
                    (nextPos to nextDepth)
                } else {
                    null
                }
            } else {
                null
            }

            val nextStates = if (nextHop != null) {
                listOf(SearchState(nextHop.first, nextHop.second, steps + 1))
            } else {
                pos.cardinalNeighbors().filter {
                    maze[it.y][it.x] == '.' && (it to depth) !in seen
                }.map {
                    SearchState(it, depth, steps + 1)
                }
            }
            queue.addAll(nextStates)
        }
        throw Exception("goal not found")
    }

    fun run() {

        println(shortestPath())
        println(shortestPathRecursive())
    }
}

fun main() {
    Day20.run()
}