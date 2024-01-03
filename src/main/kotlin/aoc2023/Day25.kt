package aoc2023

import java.io.File

private typealias Graph = Map<String, Set<String>>

fun main() {

    fun parseInput(filename: String) = File(filename).readLines()
        .map { line ->
            line.split(" ", ":").filter { it.isNotEmpty() }
        }.flatMap { tokens ->
            tokens.drop(1).flatMap { neighbor ->
                listOf(
                    tokens.first() to neighbor,
                    neighbor to tokens.first(),
                )
            }
        }.groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }

    fun Graph.removeEdges(edges: Iterable<Pair<String, String>>): Graph {
        val newGraph = this.mapValues { it.value.toMutableSet() }
        for ((src, dst) in edges) {
            newGraph[src]?.remove(dst)
            newGraph[dst]?.remove(src)
        }
        return newGraph
    }

    fun Graph.reachable(src: String): Int {
        val q = ArrayDeque<String>()
        q.add(src)
        val seen = mutableSetOf<String>()
        while (q.isNotEmpty()) {
            val node = q.removeFirst()
            seen.add(node)
            val next = this[node]?.filter { it !in seen } ?: emptySet()
            q.addAll(next)
        }
        return seen.size
    }

    fun shortestPath(src: String, dst: String, graph: Graph): List<String> {
        val q = ArrayDeque<Pair<String, List<String>>>()
        val seen = mutableSetOf<String>()
        q.add(src to emptyList())

        while (q.isNotEmpty()) {
            val (node, path) = q.removeFirst()

            if (node == dst) {
                return path + dst
            } else {
                if (node in seen) continue
                seen.add(node)

                val nextNodes = graph[node]?.filter {
                    it !in seen
                }?.map {
                    it to (path + node)
                } ?: emptySet()

                q.addAll(nextNodes)
            }
        }
        return emptyList()
    }

    fun allPaths(src: String, dst: String, graph: Graph): List<List<String>> {
        var newGraph = graph
        val paths = mutableListOf<List<String>>()
        while (paths.size < 4) {
            val shortest = shortestPath(src, dst, newGraph)
            if (shortest.isEmpty()) {
                break
            }
            paths.add(shortest)
            val edges = shortest.zipWithNext()
            newGraph = newGraph.removeEdges(edges)
        }
        return paths
    }

    fun findIslandSizes(graph: Graph): Pair<Int, Int> {
        val nodePairs = graph.keys.flatMap { src ->
            (graph.keys - src).mapNotNull { dst -> if (src < dst) src to dst else null }
        }.shuffled()

        var workingGraph = graph
        for ((src, dst) in nodePairs) {
            val paths = allPaths(src, dst, workingGraph)

            if (paths.size < 4) {
                paths.forEach { path ->
                    val edges = path.zipWithNext()
                    workingGraph = workingGraph.removeEdges(edges)
                }
                return workingGraph.reachable(src) to workingGraph.reachable(dst)
            }
        }
        return -1 to -1
    }

    val graph = parseInput("input.txt")
    val (size1, size2) = findIslandSizes(graph)
    println(size1 * size2)
}