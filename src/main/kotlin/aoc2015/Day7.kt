package aoc2015

import aoc2015.Day7.Op.*
import java.io.File

private object Day7 {
    enum class Op {
        SET,
        AND,
        OR,
        NOT,
        LSHIFT,
        RSHIFT
    }

    data class Node(
        val id: String,
        val op: Day7.Op,
        val inputs: List<String>
    ) {
        fun eval(values: List<Int>): Int {
            return when (op) {
                SET -> values[0]
                AND -> values[0] and values[1]
                OR -> values[0] or values[1]
                NOT -> values[0].inv() and 0xFFFF
                LSHIFT -> values[0] shl values[1]
                RSHIFT -> values[0] ushr values[1]
            }
        }
    }

    fun parseNode(line: String): Node {
        val tokens = line.split(" ")
        return when {
            tokens.size == 3 -> Node(tokens.last(), SET, tokens.take(1))
            tokens.first() == "NOT" -> Node(tokens.last(), NOT, listOf(tokens[1]))
            tokens[1] == "AND" -> Node(tokens.last(), AND, listOf(tokens[0], tokens[2]))
            tokens[1] == "OR" -> Node(tokens.last(), OR, listOf(tokens[0], tokens[2]))
            tokens[1] == "LSHIFT" -> Node(tokens.last(), LSHIFT, listOf(tokens[0], tokens[2]))
            tokens[1] == "RSHIFT" -> Node(tokens.last(), RSHIFT, listOf(tokens[0], tokens[2]))
            else -> throw Exception("error parsing line: $line")
        }
    }

    fun parseInput(): Map<String, Node> {
        return File("input.txt").useLines { lines ->
            lines.map { line -> parseNode(line) }
                .associateBy { it.id }
        }
    }

    fun String.symbolValue(values: Map<String, Int?>): Int? {
        return if (this in values) {
            values[this]
        } else {
            this.toInt()
        }
    }

    fun evalSignal(signalId: String, nodes: Map<String, Node>): Int {
        val signalValues = mutableMapOf<String, Int?>()
        nodes.keys.associateWithTo(signalValues) { null }
        val stack = ArrayDeque<Node>()
        stack.addLast(nodes[signalId]!!)
        outer@while (stack.isNotEmpty()) {
            val node = stack.removeLast()
            val (id, _, inputs) = node
            val inputValues = inputs.associateWith { it.symbolValue(signalValues) }
            for (input in inputValues) {
                if (input.value == null) {
                    stack.add(node)
                    stack.add(nodes[input.key]!!)
                    continue@outer
                }
            }
            val value = node.eval(inputValues.values.filterNotNull())
            signalValues[id] = value
        }
        return signalValues[signalId]!!
    }

    fun part1and2(nodes: Map<String, Node>) {
        val result = evalSignal("a", nodes)
        println(result)
        val newNodes = nodes + Pair("b", Node("b", SET, listOf(result.toString())))
        val nextResult = evalSignal("a", newNodes)
        println(nextResult)
    }

}

fun main() {
    val nodes = Day7.parseInput()
    Day7.part1and2(nodes)
}
