package aoc2017

import util.input
import kotlin.streams.asSequence

private class UnionFind(initialSize: Int) {

    private var sizes = Array(initialSize) { 1 }
    private val groups = Array(initialSize) { it }

    var size = initialSize
        private set

    fun size(item: Int) = sizes[find(item)]

    fun find(item: Int): Int {
        var root = item
        while (root != groups[root]) {
            root = groups[root]
        }
        var node = item
        while (node != root) {
            val next = groups[node]
            groups[node] = root
            node = next
        }
        return root
    }

    fun union(item1: Int, item2: Int) {
        val root1 = find(item1)
        val root2 = find(item2)

        if (root1 == root2) return

        if (sizes[root1] > sizes[root2]) {
            groups[root1] = root2
            sizes[root2] += sizes[root1]
        } else {
            groups[root2] = root1
            sizes[root1] += sizes[root2]
        }
        size--
    }
}

private fun parseLine(line: String): Pair<Int, List<Int>> {
    val tokens = line.split(" <-> ")
    return tokens[0].toInt() to tokens[1].split(", ").map { it.toInt() }
}

private fun findGroups() {
    val groups = UnionFind(2000)
    input("inputs/2017/12.txt").use { lines ->
        lines.asSequence().map { parseLine(it) }.forEach { (parent, children) ->
            children.forEach { child -> groups.union(parent, child) }
        }
    }
    println("group 0 size: " + groups.size(0))
    println("total groups: " + groups.size)
}

fun main() {
    findGroups()
}