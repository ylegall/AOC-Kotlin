package aoc2021

import java.io.File
import kotlin.math.max

fun main() {

    data class Node(
        var left: Node? = null,
        var right: Node? = null,
        var parent: Node? = null,
        var value: Int = 0
    ) {
        fun isLeaf() = left == null && right == null

        override fun toString(): String {
            return if (isLeaf()) {
                value.toString()
            } else {
                "[${left.toString()}, ${right.toString()}]"
            }
        }

        fun magnitude(): Long {
            return if (isLeaf()) {
                value.toLong()
            } else {
                3 * left!!.magnitude() + 2 * right!!.magnitude()
            }
        }

    }

    fun leafNode(value: Int, parent: Node? = null) = Node(null, null, parent, value)

    fun parseNode(chars: Iterator<Char>): Node {
        val node = Node()
        var nextChar = chars.next()
        node.left = when (nextChar) {
            '['           -> parseNode(chars)
            in '0' .. '9' -> leafNode(nextChar.digitToInt())
            else          -> throw Exception("bad char: $nextChar")
        }
        check(chars.next() == ',') { "expecting ','" }
        nextChar = chars.next()
        node.right = when (nextChar) {
            '['           -> parseNode(chars)
            in '0' .. '9' -> leafNode(nextChar.digitToInt())
            else          -> throw Exception("bad char: $nextChar")
        }
        check(chars.next() == ']') { "expecting ']'" }
        node.left?.parent = node
        node.right?.parent = node
        return node
    }

    fun parseTree(line: String): Node {
        val chars = line.drop(1).iterator()
        return parseNode(chars)
    }

    fun serialize(node: Node): List<Pair<Node, Int>> {
        val nodes = mutableListOf<Pair<Node, Int>>()
        fun recurse(node: Node, depth: Int = 0) {
            if (node.isLeaf()) {
                nodes.add(node to depth)
            } else {
                recurse(node.left!!, depth + 1)
                recurse(node.right!!, depth + 1)
            }
        }
        recurse(node)
        return nodes
    }

    fun explode(nodes: List<Pair<Node, Int>>): Boolean {
        val explodeIndex = nodes.indexOfFirst { it.second > 4 }
        return if (explodeIndex >= 0) {
            val leftExplodeValue = nodes[explodeIndex].first.value
            nodes.getOrNull(explodeIndex - 1)?.let { (node, _) ->
                node.value += leftExplodeValue
            }
            val rightExplodeValue = nodes[explodeIndex + 1].first.value
            nodes.getOrNull(explodeIndex + 2)?.let { (node, _) ->
                node.value += rightExplodeValue
            }
            val explodedNode = nodes[explodeIndex].first.parent!!
            val explodedParent = explodedNode.parent!!
            if (explodedParent.left === explodedNode) {
                explodedParent.left = leafNode(0, explodedParent)
            } else {
                explodedParent.right = leafNode(0, explodedParent)
            }
            true
        } else {
            false
        }
    }

    fun split(nodes: List<Pair<Node, Int>>): Boolean {
        for ((node, _) in nodes) {
            if (node.isLeaf() && node.value >= 10) {
                val newLeft = leafNode(node.value / 2, node)
                val newRight = leafNode(node.value - newLeft.value, node)
                node.left = newLeft
                node.right = newRight
                node.value = 0
                return true
            }
        }
        return false
    }

    fun reduce(node: Node) {
        do {
            var changed = false
            val nodes = serialize(node)
            if (explode(nodes)) {
                changed = true
            } else if (split(nodes)) {
                changed = true
            }
        } while (changed)
    }

    fun add(node1: Node, node2: Node): Node {
        val node = Node(node1, node2)
        node1.parent = node
        node2.parent = node
        reduce(node)
        return node
    }

    fun part1(lines: List<String>): Long {
        val nodes = lines.map { line -> parseTree(line) }.toList()
        var sum = nodes[0]
        for (i in 1 until nodes.size) {
            sum = add(sum, nodes[i])
        }
        return sum.magnitude()
    }

    fun part2(lines: List<String>): Long {
        var maxMagnitude = 0L
        for (i in lines.indices) {
            for (j in lines.indices) {
                if (i != j) {
                    val sum = add(parseTree(lines[i]), parseTree(lines[j]))
                    maxMagnitude = max(maxMagnitude, sum.magnitude())
                }
            }
        }
        return maxMagnitude
    }

    val nodes = File("inputs/2021/input.txt").readLines()

    println(part1(nodes))
    println(part2(nodes))
}