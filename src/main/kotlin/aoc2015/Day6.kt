package aoc2015

import aoc2015.Day6.Operation.OFF
import aoc2015.Day6.Operation.ON
import aoc2015.Day6.Operation.TOGGLE
import util.Point
import java.io.File
import kotlin.math.max

private object Day6 {

    val pointPattern = Regex("""(\d+)""")

    enum class Operation { ON, OFF, TOGGLE }

    const val LEN = 1000

    data class Command(
        val operation: Operation,
        val p1: Point,
        val p2: Point,
    )

    fun parseInput(): List<Command> {
        return File("input.txt").useLines { lines ->
            lines.map { line ->
                val points = pointPattern.findAll(line).map { it.value.toInt() }.toList()
                val op = when {
                    line.startsWith("toggle") -> TOGGLE
                    line.startsWith("turn on") -> ON
                    line.startsWith("turn off") -> OFF
                    else -> throw Exception("error parsing line: $line")
                }
                Command(op, Point(points[0], points[1]), Point(points[2], points[3]))
            }.toList()
        }
    }

    fun Array<IntArray>.set(value: Int, p1: Point, p2: Point) {
        for (row in p1.y .. p2.y) {
            for (col in p1.x .. p2.x) {
                this[row][col] = value
            }
        }
    }

    fun Array<IntArray>.add(value: Int, p1: Point, p2: Point, mod2: Boolean) {
        for (row in p1.y .. p2.y) {
            for (col in p1.x .. p2.x) {
                if (mod2) {
                    this[row][col] = (max(this[row][col] + value, 0) % 2)
                } else {
                    this[row][col] = max(this[row][col] + value, 0)
                }
            }
        }
    }

    fun part1(input: List<Command>) {
        val lights = Array(LEN) { IntArray(LEN) }
        for (command in input) {
            when (command.operation) {
                ON -> lights.set(1, command.p1, command.p2)
                OFF -> lights.set(0, command.p1, command.p2)
                TOGGLE -> lights.add(1, command.p1, command.p2, true)
            }
        }
        println(lights.sumOf { it.sum() })
    }

    fun part2(input: List<Command>) {
        val lights = Array(LEN) { IntArray(LEN) }
        for (command in input) {
            when (command.operation) {
                ON -> lights.add(1, command.p1, command.p2, false)
                OFF -> lights.add(-1, command.p1, command.p2, false)
                TOGGLE -> lights.add(2, command.p1, command.p2, false)
            }
        }
        println(lights.sumOf { it.sum() })
    }
}

fun main() {
    val input = Day6.parseInput()
    Day6.part1(input)
    Day6.part2(input)
}