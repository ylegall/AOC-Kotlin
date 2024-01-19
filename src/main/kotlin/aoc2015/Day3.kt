package aoc2015

import util.Point
import java.io.File

fun main() {

    val input = File("input.txt").readText().map { dir ->
        when (dir) {
            '<' -> Point(-1, 0)
            '>' -> Point(1, 0)
            '^' -> Point(0, -1)
            'v' -> Point(0, 1)
            else -> throw Exception("unknown input: $dir")
        }
    }

    fun part1() {
        val points = input.scan(Point(0, 0)) { acc, dir -> acc + dir }.toSet()
        println(points.size)
    }

    fun part2() {
        val pairs = input.chunked(2)
        val visited = (
                pairs.map { it[0] }.scan(Point(0, 0)) { acc, dir -> acc + dir }.toSet() +
                pairs.map { it[1] }.scan(Point(0, 0)) { acc, dir -> acc + dir }.toSet()
        ).size
        println(visited)
    }

    part1()
    part2()

}