package aoc2023

import util.lcm
import java.io.File


fun main() {

    val regex = Regex("""(\w+) = \((\w+), (\w+)\)""")

    class Input(
        val directions: String,
        val nodes: Map<String, Pair<String, String>>
    )

    fun parseInput(filename: String): Input {
        return File(filename).readLines().let { lines ->
            val directions = lines.take(2).first()
            val nodes = lines.drop(2).associate { line ->
                val (a, b, c) = regex.matchEntire(line)!!.destructured
                Pair(a, Pair(b, c))
            }
            Input(directions, nodes)
        }
    }

    fun simulate(
        startNode: String,
        input: Input,
        endCondition: (String) -> Boolean
    ): Long {
        var dirIndex = 0
        var steps = 0L
        var node = startNode
        while (!endCondition(node)) {
            val (left, right) = input.nodes[node]!!
            val dir = input.directions[dirIndex]
            node = if (dir == 'R') right else left
            steps += 1
            dirIndex = (dirIndex + 1) % input.directions.length
        }
        return steps
    }

    fun part1() {
        val input = parseInput("input.txt")
        val steps = simulate("AAA", input) { it == "ZZZ" }
        println(steps)
    }

    fun part2() {
        val input = parseInput("input.txt")
        val result = input.nodes.keys
            .filter { it.endsWith('A') }
            .map { startNode -> simulate(startNode, input) { it.endsWith('Z')} }
            .reduce { a, b -> lcm(a, b) }
        println(result)
    }

    part1()
    part2()

}