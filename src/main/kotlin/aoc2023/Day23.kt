package aoc2023

import aoc2023.Day23.findLongestPath
import util.Point
import util.get
import util.getOrNull
import java.io.File
import java.util.BitSet
import java.util.PriorityQueue
import kotlin.time.measureTimedValue

private object Day23 {

    data class Input(
        val start: Point,
        val end: Point,
        val grid: List<String>
    )

    fun validDirection(p1: Point, p2: Point, c: Char): Boolean {
        return when (c) {
            '>' -> p1.x < p2.x
            '<' -> p1.x > p2.x
            'v' -> p1.y < p2.y
            '^' -> p1.y > p2.y
            else -> true
        }
    }

    data class Edge(
        val src: Int,
        val dst: Int,
        val length: Int
    )

    class Graph(
        val nodes: List<Point>,
    ) {
        val nodeIds = nodes.indices.associateBy { nodes[it] }
        val edges = mutableMapOf<Int, List<Edge>>()

        fun findEdges(src: Point, input: Input, bidirectional: Boolean = false) {
            val srcId = nodeIds[src]!!
            val q = ArrayDeque<Path>()
            q.addFirst(Path(src))
            while (q.isNotEmpty()) {
                val (pos, dist, seen) = q.removeFirst()
                if (pos in nodeIds && pos != src) {
                    val dstId = nodeIds[pos]!!
                    edges[srcId] = (edges[srcId] ?: emptyList()) + Edge(srcId, dstId, dist)
                } else {
                    pos.cardinalNeighbors().filter {
                        it.y in 0 .. input.grid.lastIndex && it !in seen
                    }.filter {
                        when (val c = input.grid[it]) {
                            '#' -> false
                            '.' -> true
                            else -> bidirectional || validDirection(pos, it, c)
                        }
                    }.forEach {
                        q.addFirst(Path(it, dist + 1, seen + pos))
                    }
                }
            }
        }
    }

    fun parseInput(): Input {
        val grid = File("input.txt").readLines()
        val start = grid.first().indexOf(".").let { Point(it, 0) }
        val end = grid.last().indexOf(".").let { Point(it, grid.lastIndex) }
        return Input(start, end, grid)
    }

    data class Path(
        val point: Point,
        val distance: Int = 0,
        val seen: Set<Point> = emptySet()
    )

    fun findNodes(input: Input): List<Point> {
        val nodes = mutableListOf(input.start, input.end)
        for (row in input.grid.indices) {
            for (col in input.grid[row].indices) {
                val point = Point(col, row)
                if (input.grid.getOrNull(point) == '.') {
                    val neighbors = point.cardinalNeighbors().mapNotNull { input.grid.getOrNull(it) }
                    if (neighbors.none { it == '.' }) {
                        nodes.add(point)
                    }
                }
            }
        }
        return nodes
    }

    fun makeGraph(
        input: Input,
        nodes: List<Point>,
        bidirectional: Boolean = false
    ): Graph {
        val graph = Graph(nodes)
        for (node in nodes) {
            graph.findEdges(node, input, bidirectional)
        }
        return graph
    }

    data class State(
        val nodeId: Int,
        val seen: BitSet,
        val length: Int = 0,
    )

    fun findLongestPath(
        input: Input,
        graph: Graph
    ): Int {
        val results = mutableMapOf<Int, Int>()
        val q = ArrayDeque<State>()
        val startId = graph.nodeIds[input.start]!!
        val endId = graph.nodeIds[input.end]!!
        q.add(State(startId, BitSet(graph.nodes.size), 0))
        while (q.isNotEmpty()) {
             val (nodeId, seen, dist) = q.removeFirst()
            if (nodeId == endId) {
                results[endId] = (results[endId] ?: 0).coerceAtLeast(dist)
            } else {
                results[nodeId] = (results[nodeId] ?: 0).coerceAtLeast(dist)
                graph.edges[nodeId]!!.filter { edge ->
                    !seen[edge.dst]
                }.map { edge ->
                    val newSeen = BitSet(graph.nodes.size).apply { or(seen) }
                    newSeen.set(nodeId)
                    State(edge.dst, newSeen, dist + edge.length)
                }.forEach { newState ->
                    q.addFirst(newState)
                }
            }
        }
        return results[endId] ?: 0
    }

}

fun main() {
    val input = Day23.parseInput()

    val nodes = Day23.findNodes(input)

    // part 1
    val graph = Day23.makeGraph(input, nodes)
    println(findLongestPath(input, graph))

    // part 2
    val graph2 = Day23.makeGraph(input, nodes, true)
    println(findLongestPath(input, graph2))
}