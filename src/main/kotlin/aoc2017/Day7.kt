package aoc2017

import util.input
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day7 {

    private fun findRoot(nodes: MutableMap<String, List<String>>): String {
        while (nodes.size > 1) {
            val iterator = nodes.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                if (node.value.all { it !in nodes.keys }) {
                    iterator.remove()
                    if (nodes.size == 1) break
                }
            }
        }
        return nodes.keys.first()
    }

    private fun parseInput(): MutableMap<String, List<String>> {
        val nodes = mutableMapOf<String, List<String>>()
        input("inputs/2017/7.txt").use { lines ->
            lines.forEach { line ->
                val parts = line.split("->")
                val parent = parts[0].slice(0 until parts[0].indexOf(' '))
                if (parts.size > 1) {
                    val children = parts[1].split(',').map { it.trim() }
                    nodes[parent] = children
                } else {
                    nodes[parent] = emptyList()
                }
            }
        }
        return nodes
    }

    @ExperimentalTime
    fun run() {
        val input = parseInput()
        println(measureTimedValue { findRoot(input) })
    }
}

@ExperimentalTime
fun main() {
    Day7.run()
}