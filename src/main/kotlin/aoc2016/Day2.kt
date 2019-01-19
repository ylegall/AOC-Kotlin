package aoc2016

import util.Point
import util.input
import kotlin.streams.asSequence

private val KEYPAD1 =
"""
1 2 3
4 5 6
7 8 9
""".drop(1)

private val KEYPAD2 =
"""
    1
  2 3 4
5 6 7 8 9
  A B C
    D
""".drop(1)

private fun buildKeymap(keypad: String): Map<Point, Char> {
    return keypad.split("\n").mapIndexed { y, line ->
        Pair(y, line)
    }.flatMap { row ->
        row.second.mapIndexed { x, c -> Triple(x, row.first, c) }
    }.filter {
        !it.third.isWhitespace()
    }.associate { triple ->
        Point(triple.first/2, triple.second) to triple.third
    }
}

private fun Point.move(dir: Char, keymap: Map<Point, Char>): Point {
    val nextPoint = when(dir) {
        'U' -> Point(x, y - 1)
        'L' -> Point(x - 1, y)
        'D' -> Point(x, y + 1)
        'R' -> Point(x + 1, y)
        else -> throw Exception("bad direction")
    }

    return if (nextPoint !in keymap) {
        this
    } else {
        nextPoint
    }
}

private fun Point.move(moves: String, keymap: Map<Point, Char>) = moves.fold(this) {
    pos, dir -> pos.move(dir, keymap)
}

private fun findCode(start: Point, keymap: Map<Point, Char>, lines: Sequence<String>): String {
    var current = start
    return lines.mapNotNull { line ->
        current = current.move(line, keymap)
        keymap[current]
    }.joinToString("")
}

fun main() {
    val code1 = input("inputs/2016/2.txt").use { lines ->
        findCode(Point(1, 1), buildKeymap(KEYPAD1), lines.asSequence())
    }
    println(code1)

    val code2 = input("inputs/2016/2.txt").use { lines ->
        findCode(Point(0, 2), buildKeymap(KEYPAD2), lines.asSequence())
    }
    println(code2)
}