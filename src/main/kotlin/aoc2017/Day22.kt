package aoc2017

import aoc2017.Day22.CellType.*
import util.Direction
import util.Point
import util.move
import java.io.File

private object Day22 {

    private enum class CellType { CLEAN, WEAK, FLAGGED, INFECTED }

    private class State(
            private val map: MutableMap<Point, CellType> = HashMap(),
            private var pos: Point = Point(0, 0),
            private var dir: Direction = Direction.NORTH
    ) {

        var newInfections = 0

        fun updatePart1() {
            dir = if (pos in map) {
                map.remove(pos)
                dir.turnRight()
            } else {
                map[pos] = INFECTED
                newInfections++
                dir.turnLeft()
            }
            pos = pos.move(dir)
        }

        fun updatePart2() {
            val cell = map[pos] ?: CLEAN
            dir = when(cell) {
                CLEAN    -> dir.turnLeft()
                WEAK     -> dir
                FLAGGED  -> dir.reverse()
                INFECTED -> dir.turnRight()
            }
            map[pos] = when(cell) {
                CLEAN    -> WEAK
                WEAK     -> INFECTED
                FLAGGED  -> CLEAN
                INFECTED -> FLAGGED
            }
            if (map[pos] == INFECTED) newInfections++
            pos = pos.move(dir)
        }
    }

    private fun parseInput(): State {
        val lines = File("inputs/2017/22.txt").readLines()
        val points = HashMap<Point, CellType>()
        for (row in lines.indices) {
            for (col in lines[row].indices) {
                if (lines[row][col] == '#') points[Point(col, row)] = INFECTED
            }
        }
        return State(points, Point(lines[0].length / 2, lines.size / 2))
    }

    fun runPart1() {
        val state = parseInput()
        repeat(10000) { state.updatePart1() }
        println(state.newInfections)
    }

    fun runPart2() {
        val state = parseInput()
        repeat(10000000) { state.updatePart2() }
        println(state.newInfections)
    }
}

fun main() {
    Day22.runPart1()
    Day22.runPart2()
}