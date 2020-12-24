package aoc2020

import java.lang.StringBuilder

private object Day23 {

    data class Node(
            val id: Int,
            var next: Node? = null
    ) {
        override fun toString() = "$id -> ${next?.id}"
    }

    fun printList(node: Node, limit: Int = 9): String {
        var count = 0
        var current = node
        val start = node.id
        val sb = StringBuilder()
        do {
            sb.append(current.id).append(", ")
            count++
            if (count >= limit) break
            current = current.next!!
        } while (current.id != start)
        return sb.toString()
    }

    fun playGame(
            node: Node,
            rounds: Int,
            maxLabel: Int,
            index: Map<Int, Node>
    ): Node {
        var current = node
        for (round in 1 .. rounds) {
            //println("-- move $round --")
            //println("cups: " + printList(current))

            // find next 3 to remove:
            val firstRemoved = current.next!!
            var lastRemoved = firstRemoved
            repeat(2) { lastRemoved = lastRemoved.next!! }
            val remaining = lastRemoved.next!!
            //println("pick up: ${printList(firstRemoved, 3)}")

            // remove 3 from circle:
            current.next = remaining

            // find destination label:
            var targetLabel = current.id
            do {
                targetLabel--
                if (targetLabel <= 0) targetLabel = maxLabel
            } while (targetLabel == firstRemoved.id || targetLabel == firstRemoved.next!!.id || targetLabel == lastRemoved.id)
            //println("destination: $targetLabel")

            // find target node
            val preInsertNode = index[targetLabel]!!

            // insert 3 removed nodes:
            val postInsertNode = preInsertNode.next!!
            preInsertNode.next = firstRemoved
            lastRemoved.next = postInsertNode

            current = current.next!!

            //println()
        }

        // find cup at 1
        while (current.id != 1) current = current.next!!
        return current
    }

    fun playGamePart1(input: String): String {
        val list = input.map { Character.digit(it, 10) }
        val nodes = list.map { Node(it) }
        val index = mutableMapOf<Int, Node>()
        nodes.forEachIndexed { i, node ->
            node.next = nodes[(i + 1) % nodes.size]
            index[node.id] = node
        }

        var resultNode = playGame(nodes[0], rounds = 100, maxLabel = 9, index)

        // find node after 1
        resultNode = resultNode.next!!
        val sb = StringBuilder()
        while (resultNode.id != 1) {
            sb.append(resultNode.id)
            resultNode = resultNode.next!!
        }
        return sb.toString()
    }

    fun playGamePart2(input: String): Long {
        val nodes = (input.map { Character.digit(it, 10) } + (10..1000000)).map { Node(it) }
        val index = mutableMapOf<Int, Node>()
        nodes.forEachIndexed { i, node ->
            node.next = nodes[(i + 1) % nodes.size]
            index[node.id] = node
        }

        var resultNode = playGame(nodes[0], rounds = 10000000, maxLabel = 1000000, index)

        // find node after 1
        resultNode = resultNode.next!!
        return resultNode.id.toLong() * resultNode.next!!.id.toLong()
    }
}

fun main() {
    //val inputDigits = "389125467" // example
    val inputDigits = "459672813"
    println(Day23.playGamePart1(inputDigits))

    println(Day23.playGamePart2(inputDigits))
}