package aoc2023

import util.Point
import java.io.File
import java.lang.StringBuilder


fun main() {

    data class NumberPosition(
        val number: Int,
        val start: Point,
        val stop: Point
    )

    data class State(
        val symbols: Set<Point>,
        val gears: Map<Point, Int>,
        val numbers: List<NumberPosition>
    )

    fun parseInput(filename: String): State {
        val symbols = mutableSetOf<Point>()
        val gears = mutableMapOf<Point, Int>()
        val numbers = mutableListOf<NumberPosition>()

        File(filename).useLines { lines ->
            lines.forEachIndexed { row, line ->
                var startPoint = Point(0, 0)
                val digitBuffer = StringBuilder()
                for (col in line.indices) {
                    val char = line[col]
                    val coord = Point(col, row)

                    if (char.isDigit()) {
                        if (digitBuffer.isEmpty()) {
                            startPoint = coord
                        }
                        digitBuffer.append(char)
                    } else {
                        if (digitBuffer.isNotEmpty()) {
                            val number = digitBuffer.toString().toInt()
                            digitBuffer.clear()
                            numbers.add(NumberPosition(number, startPoint, Point(col-1, row)))
                        }
                        if (char != '.') {
                            symbols.addAll(coord.mooreNeighbors())
                            if (char == '*') {
                                val id = gears.size
                                gears[coord] = id
                            }
                        }
                    }
                }

                if (digitBuffer.isNotEmpty()) {
                    val number = digitBuffer.toString().toInt()
                    digitBuffer.clear()
                    numbers.add(NumberPosition(number, startPoint, Point(line.length-1, row)))
                }
            }
        }
        return State(symbols, gears, numbers)
    }

    fun part1() {
        val (symbols, gears, numbers) = parseInput("input.txt")
        val result = numbers.sumOf { (number, start, stop) ->
            val numberPoints = (start.x..stop.x).map { x -> Point(x, start.y) }
            if (numberPoints.any { it in symbols }) {
                number
            } else {
                0
            }
        }
        println(result)
    }

    fun part2() {
        val (symbols, gears, numbers) = parseInput("input.txt")
        val gearNumbers = mutableMapOf<Int, List<Long>>()
        for (entry in numbers) {
            val numberPoints = (entry.start.x .. entry.stop.x).map { x -> Point(x, entry.start.y) }
            val adjacentGears = numberPoints.flatMap { it.mooreNeighbors() }.distinct().mapNotNull { gears[it] }
            for (gearId in adjacentGears) {
                val numberList = gearNumbers[gearId] ?: emptyList()
                gearNumbers[gearId] = numberList + entry.number.toLong()
            }
        }

        val result = gearNumbers.values.sumOf {
            if (it.size == 2) {
                it.reduce { a, b -> a * b }
            } else {
                0L
            }
        }
        println(result)
    }

    part1()
    part2()
}
