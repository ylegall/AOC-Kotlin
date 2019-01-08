package aoc2015

import util.input
import java.util.*
import kotlin.streams.asSequence

class Circuit
{
    private class Node(
        val operation: String,
        val symbol: String,
        val children: List<String>
    )

    private val nodes = HashMap<String, Node>()

    fun buildNodes(lines: Sequence<String>) {
        lines.forEach { line ->
            val tokens = line.split(" ")
            val node = when {
                tokens.size == 3 -> Node("SET", tokens.last(), listOf(tokens.first()))
                tokens[0] == "NOT" -> Node("NOT", tokens.last(), listOf(tokens[1]))
                tokens[1] == "AND" -> Node("AND", tokens.last(), listOf(tokens[0], tokens[2]))
                tokens[1] == "OR" -> Node("OR", tokens.last(), listOf(tokens[0], tokens[2]))
                tokens[1] == "LSHIFT" -> Node("LSHIFT", tokens.last(), listOf(tokens[0], tokens[2]))
                tokens[1] == "RSHIFT" -> Node("RSHIFT", tokens.last(), listOf(tokens[0], tokens[2]))
                else -> throw Exception("invalid command")
            }
            nodes[node.symbol] = node
        }
    }

    fun evalTree(root: String, overrides: List<Pair<String, Int>> = emptyList()): Int {
        val values = HashMap<String, Int>()
        overrides.forEach { values[it.first] = it.second }
        return evalTree(root, values)
    }

    private fun evalTree(symbol: String, values: HashMap<String, Int>): Int {
        if (symbol in values) {
            return values[symbol]!!
        }
        if (symbol !in nodes) {
            return symbol.toInt()
        }
        val node = nodes[symbol] ?: throw Exception("symbol $symbol not found")
        val result = when (node.operation) {
            "SET" -> if (node.children.first() in nodes) evalTree(node.children.first(), values) else node.children.first().toInt()
            "NOT" -> evalTree(node.children.first(), values).inv()
            "AND" -> evalTree(node.children.first(), values) and evalTree(node.children.last(), values)
            "OR" -> evalTree(node.children.first(), values) or evalTree(node.children.last(), values)
            "LSHIFT" -> evalTree(node.children.first(), values) shl node.children.last().toInt()
            "RSHIFT" -> evalTree(node.children.first(), values) ushr node.children.last().toInt()
            else -> throw Exception("invalid operation: ${node.operation}")
        }
        if (node.symbol in values) throw Exception("duplicate result for ${node.symbol}")
        values[node.symbol] = result
        return result
    }
}


fun main() {
    val circuit = Circuit()
    input("inputs/2015/7.txt").use {
        circuit.buildNodes(it.asSequence())
    }
    val result = circuit.evalTree("a")
    println(result)
    println(circuit.evalTree("a", listOf(Pair("b", result))))
}