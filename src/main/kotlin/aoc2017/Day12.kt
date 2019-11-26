package aoc2017

import util.UnionFind
import util.input
import kotlin.streams.asSequence


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