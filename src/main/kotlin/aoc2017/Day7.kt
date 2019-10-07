package aoc2017

import util.input

private object Day7 {

    private data class Node(
            val name: String,
            val weight: Int
    )

    private class WeirdTree(
            val root: String,
            val nodes: Map<String, Node>,
            val children: Map<String, List<String>>
    ) {
        fun children(name: String) = children[name]?.mapNotNull { nodes[it] } ?: emptyList()
    }

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
    private fun findWeightDiscrepancy(tree: WeirdTree): Int {
        val correctedWeights = ArrayList<Int>()
        findWeightDiscrepancy(tree.root, tree, correctedWeights)
        return correctedWeights.first()
    }

    private fun findWeightDiscrepancy(
            node: String,
            tree: WeirdTree,
            correctedWeights: ArrayList<Int>
    ): Int {
        val nodeWeight = tree.nodes[node]?.weight ?: 0
        val children = tree.children(node)
        return if (children.isEmpty()) {
            nodeWeight
        } else {
            val weights = children.map { it to findWeightDiscrepancy(it.name, tree, correctedWeights) }
            val outlier = findNodeWithWrongWeight(weights)
            if (outlier != null) {
                if (correctedWeights.isEmpty()) {
                    println("\nfound weight outlier in children of $node!")
                    //println(weights)
                    println("outlier node: ${outlier.first}")
                    println("weight difference: ${outlier.second}")
                    println("correct weight should be: " + (outlier.first.weight + outlier.second))
                    correctedWeights.add(outlier.first.weight + outlier.second)
                }
            }
            val childWeights = weights.map { it.second }.sum()
            val totalSubtreeWeight = nodeWeight + childWeights
            totalSubtreeWeight
        }
    }

    private fun findNodeWithWrongWeight(nodes: List<Pair<Node, Int>>): Pair<Node, Int>? {
        val weightCounts = nodes.groupingBy { it.second }.eachCount()
        return when {
            weightCounts.size == 1 -> null
            weightCounts.size == 2 -> {
                val correctWeight = weightCounts.maxBy { it.value }?.key!!
                val wrongWeight = weightCounts.minBy { it.value }?.key!!
                val wrongNode = nodes.find { it.second != correctWeight }!!.first
                wrongNode to correctWeight - wrongWeight
            }
            else -> throw Exception("invalid weight distributions: $nodes")
        }
    }

    private fun parseInput(): WeirdTree {
        val children = mutableMapOf<String, List<String>>()
        val nodes = mutableMapOf<String, Node>()

        input("inputs/2017/7.txt").use { lines ->
            lines.forEach { line ->
                val tokens = line.split("(", "->", ")", ",").map { it.trim() }.filter { it.isNotEmpty() }
                if (tokens.size > 2) {
                    children[tokens[0]] = tokens.drop(2)
                }
                nodes[tokens[0]] = Node(tokens[0], tokens[1].toInt())
            }
        }

        val root = findRoot(children.toMutableMap())
        return WeirdTree(root, nodes, children)
    }

    fun run() {
        val tree = parseInput()
        println(tree.root)
        println(findWeightDiscrepancy(tree))
    }
}

fun main() {
    Day7.run()
}