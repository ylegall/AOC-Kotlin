package aoc2022

import util.product
import util.split
import java.io.File


fun main() {

    class Monkey(
        val items: ArrayDeque<Long>,
        val worryFunction: List<String>,
        val divisorTest: Int,
        val trueNeighbor: Int,
        val falseNeighbor: Int
    ) {
        override fun toString(): String {
            return """
                Monkey(items=$items)
            """.trimIndent()
        }
    }

    fun parseInput(): List<Monkey> {
        return File("input.txt").useLines { lines ->
            lines.toList().split { it.isEmpty() }
                .map { chunk ->
                    val startingItems = chunk[1].removePrefix("  Starting items: ").split(", ").map { it.toLong() }
                    val operation = chunk[2].substringAfterLast("= ").split(" ").drop(1)
                    val test = chunk[3].substringAfterLast(" ").toInt()
                    val trueNeighbor = chunk[4].substringAfterLast(" ").toInt()
                    val falseNeighbor = chunk[5].substringAfterLast(" ").toInt()
                    Monkey(
                        ArrayDeque(startingItems),
                        operation,
                        test,
                        trueNeighbor,
                        falseNeighbor
                    )
                }
        }
    }

    val input = parseInput()

    fun evalWorryLevel(level: Long, expression: List<String>): Long {
        val rhs = if (expression[1] == "old") level else expression[1].toLong()
        return when (val op = expression[0][0]) {
            '*' -> level * rhs
            '+' -> level + rhs
            else -> throw Exception("invalid op: $op")
        }
    }

    fun turn(monkey: Monkey, monkeys: List<Monkey>, reduceWorry: Boolean) {
        val mod = monkeys.fold(1L) { product, mky -> product * mky.divisorTest }
        while (monkey.items.isNotEmpty()) {
            val item = monkey.items.removeFirst()
            var worryLevel = evalWorryLevel(item, monkey.worryFunction)
            if (reduceWorry) {
                worryLevel /= 3L
            } else {
                worryLevel %= mod
            }
            val nextMonkeyIndex = if (worryLevel % monkey.divisorTest == 0L) monkey.trueNeighbor else monkey.falseNeighbor
            monkeys[nextMonkeyIndex].items.addLast(worryLevel)
        }
    }

    fun simulate(turns: Int, reduceWorry: Boolean): Long {
        val totalItemCounts = LongArray(input.size)
        for (round in 0 until turns) {
            for (i in input.indices) {
                totalItemCounts[i] += input[i].items.size.toLong()
                turn(input[i], input, reduceWorry)
            }
        }
        return totalItemCounts.sorted().takeLast(2).product()
    }

    fun part1() {
        println(simulate(20, reduceWorry = true))
    }

    fun part2() {
        println(simulate(10000, reduceWorry = false))
    }

    part1()
    part2()
}
