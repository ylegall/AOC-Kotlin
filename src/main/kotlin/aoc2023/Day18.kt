package aoc2023

import util.Point
import java.io.File
import kotlin.math.abs

private object Day18 {

    enum class Dir { R, D, L, U }

    data class Move(
        val dir: Dir,
        val length: Int,
        val colorCode: String
    )

    fun parseInput(): List<Move> {
        return File("input.txt").readLines().map { line ->
            val (dir, length, code) = line.split(" ")
            Move(Dir.valueOf(dir), length.toInt(), code.drop(2).dropLast(1))
        }
    }

    fun makeBoundary(moves: List<Move>): List<Point> {
        var pos = Point(0, 0)
        return moves.map { move ->
            when (move.dir) {
                Dir.U -> Point(pos.x, pos.y - move.length)
                Dir.D -> Point(pos.x, pos.y + move.length)
                Dir.L -> Point(pos.x - move.length, pos.y)
                Dir.R -> Point(pos.x + move.length, pos.y)
            }.also {
                pos = it
            }
        }.let {
            listOf(Point(0, 0)) + it
        }
    }

    fun perimeter(boundary: List<Point>): Int {
        return boundary.windowed(2).sumOf { (p1, p2) ->
            abs(p2.x - p1.x) + abs(p2.y - p1.y)
        }
    }

    fun polygonArea(boundary: List<Point>): Long {
        return boundary.windowed(2).sumOf { (p1, p2) ->
            (p1.y + p2.y).toLong() * (p1.x - p2.x) / 2L
        }
    }

    fun part1(input: List<Move>) {
        val boundary = makeBoundary(input)
        println(perimeter(boundary)/2 + polygonArea(boundary) + 1)
    }

    fun part2(input: List<Move>) {
        val newInput = input.map { move ->
            val dir = Dir.entries[move.colorCode.last().digitToInt()]
            val length = move.colorCode.dropLast(1).toInt(16)
            Move(dir, length, move.colorCode)
        }
        part1(newInput)
    }
}

fun main() {
    val input = Day18.parseInput()
    Day18.part1(input)
    Day18.part2(input)
}