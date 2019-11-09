package aoc2018

import java.io.File
import java.util.*

private object Day8 {

    private data class Node(val children: List<Node>, val metadata: List<Int>)

    private fun sumMetadata(root: Node): Int {
        var sum = 0
        val stack = ArrayDeque<Node>()
        stack.add(root)
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            sum += node.metadata.sum()
            node.children.forEach { stack.push(it) }
        }
        return sum
    }

    private fun getNodeValue(node: Node): Int {
        return if (node.children.isEmpty()) {
            node.metadata.sum()
        } else {
            node.metadata.filter {
                it <= node.children.size
            }.map {
                getNodeValue(node.children[it - 1])
            }.sum()
        }
    }

    private fun parse(items: Iterator<Int>): Node {
        val children = arrayListOf<Node>()
        val metadata = arrayListOf<Int>()
        val numChildren = items.next()
        val numMetadata = items.next()
        for (i in 0 until numChildren) {
            children.add(parse(items))
        }
        for (i in 0 until numMetadata) {
            metadata.add(items.next())
        }
        return Node(children, metadata)
    }

    fun run() {
        val items = File("inputs/2018/8.txt").useLines { lines ->
            lines.first().split(" ").map { it.toInt() }
        }
        val root = parse(items.iterator())
        println(sumMetadata(root))
        println(getNodeValue(root))
    }
}

fun main() {
    Day8.run()
}