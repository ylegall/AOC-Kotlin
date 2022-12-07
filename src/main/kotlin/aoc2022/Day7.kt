package aoc2022

import java.io.File

fun main() {

    class Node(
        val name: String,
        val size: Long = 0
    ) {
        val children = mutableMapOf<String, Node>()

        fun isDir() = children.isNotEmpty()

        override fun toString(): String {
            val type = if (isDir()) "dir" else "file"
            val size = if (isDir()) "" else ", size=$size"
            return "- $name ($type$size)"
        }
    }

    val input = File("input.txt").useLines { lines ->
        lines.drop(1).map { it.split(" ") }.toList()
    }

    fun buildDirectoryTree(cursor: Iterator<List<String>>): Node {
        val nodeStack = ArrayDeque<Node>()
        nodeStack.addLast(Node("/"))

        while (cursor.hasNext()) {
            val line = cursor.next()
            if (line[0] == "$") {
                when (val command = line[1]) {
                    "ls" -> {}
                    "cd" -> when (line[2]) {
                        ".." -> nodeStack.removeLast()
                        else -> {
                            val nextNode = nodeStack.last().children[line[2]]!!
                            nodeStack.addLast(nextNode)
                        }
                    }
                    else -> throw Exception("bad command: $command")
                }
            } else {
                val name = line[1]
                val size = if (line[0] == "dir") 0 else line[0].toLong()
                nodeStack.last().children[name] = Node(name, size)
            }
        }
        return nodeStack.first()
    }

    fun printDirectory(node: Node, level: Int = 0) {
        val indent = "  ".repeat(level)
        println("$indent$node")
        for (child in node.children) {
            printDirectory(child.value, level + 1)
        }
    }

    fun getDirectorySizes(root: Node): List<Long> {
        val sizes = mutableListOf<Long>()

        fun calculateSize(node: Node): Long {
            val size = if (node.isDir()) {
                node.children.values.sumOf { calculateSize(it) }
            } else {
                node.size
            }
            if (node.isDir()) {
                sizes.add(size)
            }
            return size
        }
        calculateSize(root)
        return sizes
    }

    fun part1() {
        val root = buildDirectoryTree(input.iterator())
        val sizes = getDirectorySizes(root)
        val totalSize = sizes.filter { it < 100000 }.sum()
        println(totalSize)
    }

    fun part2() {
        val root = buildDirectoryTree(input.iterator())
        val sizes = getDirectorySizes(root).sorted()
        val freeSpace = 70000000L - sizes.last()
        val deletionSize = 30000000L - freeSpace
        println(sizes.find { it >= deletionSize })
    }

    part1()
    part2()
}