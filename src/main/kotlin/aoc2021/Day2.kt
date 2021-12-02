package aoc2021

import java.io.File


fun main() {

    data class Submarine1(
        var depth: Long,
        var x: Long,
    ) {
        fun move(command: String, amount: Long) {
            when (command) {
                "up" -> depth -= amount
                "down" -> depth += amount
                "forward" -> x += amount
            }
        }
    }

    data class Submarine2(
        var depth: Long,
        var x: Long,
        var aim: Long
    ) {
        fun move(command: String, amount: Long) {
            when (command) {
                "up" -> aim -= amount
                "down" -> aim += amount
                "forward" -> {
                    x += amount
                    depth += (aim * amount)
                }
            }
        }
    }

    fun part1(commands: List<Pair<String, Long>>): Long {
        val sub = Submarine1(0, 0)
        commands.forEach { (dir, amount) -> sub.move(dir, amount) }
        return sub.x * sub.depth
    }

    fun part2(commands: List<Pair<String, Long>>): Long {
        val sub = Submarine2(0, 0, 0)
        commands.forEach { (dir, amount) -> sub.move(dir, amount) }
        return sub.x * sub.depth
    }

    fun parseLine(line: String): Pair<String, Long> {
        val tokens = line.split(" ")
        return tokens[0] to tokens[1].toLong()
    }

    fun parseInput(): List<Pair<String, Long>> {
        return File("inputs/2021/2.txt").useLines { lines ->
            lines.map { parseLine(it) }.toList()
        }
    }

    val input = parseInput()

    println(part1(input))
    println(part2(input))
}