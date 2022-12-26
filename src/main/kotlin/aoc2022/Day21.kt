package aoc2022

import aoc2022.Day21.Node
import aoc2022.Day21.Num
import aoc2022.Day21.Op
import java.io.File

object Day21 {
    sealed class Node(open val name: String)

    data class Op(
        override val name: String,
        val left: Node,
        val right: Node,
        val op: Char
    ): Node(name)

    data class Num(
        override val name: String,
        val value: Long
    ): Node(name)
}

fun main() {

    val input = File("input.txt").useLines { lines ->
        lines.map { line ->
            val tokens = line.split(" ", ": ")
            if (tokens.size == 2) {
                listOf(tokens[0], tokens[1])
            } else {
                listOf(tokens[0], tokens[1], tokens[2], tokens[3])
            }
        }.associateBy { it[0] }
    }

    fun buildTree(name: String): Node {
        val tokens = input[name]!!
        return when (tokens.size) {
            2    -> Num(tokens[0], tokens[1].toLong())
            else -> Op(tokens[0], buildTree(tokens[1]), buildTree(tokens[3]), tokens[2][0])
        }
    }

    fun evalTree(node: Node): Long {
        return when (node) {
            is Num -> node.value
            is Op  -> when (node.op) {
                '*'  -> evalTree(node.left) * evalTree(node.right)
                '/'  -> evalTree(node.left) / evalTree(node.right)
                '+'  -> evalTree(node.left) + evalTree(node.right)
                '-'  -> evalTree(node.left) - evalTree(node.right)
                else -> throw Exception("invalid op")
            }
        }
    }

    fun solve(root: Op): Long {

        fun containsHuman(node: Node): Boolean {
            return when (node) {
                is Num -> node.name == "humn"
                is Op -> containsHuman(node.left) || containsHuman(node.right)
            }
        }

        var left = root.left
        var right = root.right

        while (true) {

            val (lhs, rhs) = if (containsHuman(left)) {
                left to Num(right.name, evalTree(right))
            } else {
                right to Num(left.name, evalTree(left))
            }
            if (lhs.name == "humn") return rhs.value
            val ll = (lhs as Op).left
            val lr = lhs.right
            if (containsHuman(ll)) {
                left = ll
                right = when (lhs.op) {
                    '*'  -> Op(rhs.name, rhs, lr, '/')
                    '/'  -> Op(rhs.name, rhs, lr, '*')
                    '+'  -> Op(rhs.name, rhs, lr, '-')
                    '-'  -> Op(rhs.name, rhs, lr, '+')
                    else -> throw Exception("invalid op")
                }
            } else {
                left = lr
                right = when (lhs.op) {
                    '*'  -> Op(rhs.name, rhs, ll, '/')
                    '/'  -> Op(rhs.name, rhs, ll, '*')
                    '+'  -> Op(rhs.name, rhs, ll, '-')
                    '-'  -> Op(ll.name, ll, rhs, '-')
                    else -> throw Exception("invalid op")
                }
            }
        }
    }

    fun part1() {
        val root = buildTree("root")
        println(evalTree(root))
    }

    fun part2() {
        val root = buildTree("root")
        val result = solve(root as Op)
        println(result)
    }

    part1()
    part2()
}