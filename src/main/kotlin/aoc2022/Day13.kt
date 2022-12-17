package aoc2022

import aoc2022.Day13.ListNode
import aoc2022.Day13.Node
import aoc2022.Day13.ValueNode
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import util.product
import java.io.File
import kotlin.math.min

object Day13 {
    sealed class Node
    data class ValueNode(val value: Int): Node()
    data class ListNode(val children: List<Node>): Node()
}

fun main() {

    fun parse(data: String): Node {
        fun buildTree(elem: JsonElement): Node {
            return if (elem.isJsonArray) {
                val array = elem.asJsonArray
                ListNode(List(array.size()) { buildTree(array[it]) })
            } else {
                ValueNode(elem.asInt)
            }
        }
        return buildTree(JsonParser.parseString(data))
    }

    val input = File("input.txt").readLines()
        .filter { it.isNotEmpty() }
        .map { parse(it) }

    fun compareNodes(n1: Node, n2: Node): Int {
        return when (n1) {
            is ListNode -> when (n2) {
                is ListNode -> {
                    for (i in 0 until min(n1.children.size, n2.children.size)) {
                        val result = compareNodes(n1.children[i], n2.children[i])
                        if (result != 0) return result
                    }
                    n1.children.size - n2.children.size
                }
                is ValueNode -> compareNodes(n1, ListNode(listOf(n2)))
            }
            is ValueNode -> when (n2) {
                is ListNode -> compareNodes(ListNode(listOf(n1)), n2)
                is ValueNode -> n1.value - n2.value
            }
        }
    }

    fun part1() {
        val sum = input.chunked(2)
            .mapIndexedNotNull { index, nodes ->
                if (compareNodes(nodes[0], nodes[1]) <= 0) (index + 1) else null
            }
            .sum()
        println(sum)
    }

    fun part2() {
        val extraPackets = listOf("[[2]]", "[[6]]").map { parse(it) }
        val product = (input + extraPackets)
            .asSequence()
            .sortedWith { n1, n2 -> compareNodes(n1, n2) }
            .mapIndexed { index, node -> (index + 1) to node }
            .filter { (_, node) -> node in extraPackets }
            .map { it.first }
            .toList()
            .product()
        println(product)
    }

    part1()
    part2()
}