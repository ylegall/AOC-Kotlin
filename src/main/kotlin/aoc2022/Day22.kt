package aoc2022

import util.Direction
import util.Point
import util.move
import util.swap
import java.io.File

fun main() {
    val (board, directions) = File("input.txt").readLines().let {
        val board = it.dropLast(2)
        val directions = it.last()
            .replace(Regex("""\d+""")) { matchResult -> ",${matchResult.value}," }
            .split(",")
            .filter { it.isNotEmpty() }
        board to directions
    }

    operator fun List<String>.get(pos: Point): Char {
        val row = getOrElse(pos.y) { "" }
        return row.getOrElse(pos.x) { ' ' }
    }

    val maxCols = board.maxOf { it.length }

    val verticalWrapPoints = (0 until maxCols).flatMap { colIndex ->
        val firstRow = board.indexOfFirst { row -> row.getOrElse(colIndex) { ' ' } != ' ' }
        val lastRow = board.indexOfLast { row -> row.getOrElse(colIndex) { ' ' } != ' ' }
        val firstPoint = Point(colIndex, firstRow)
        val lastPoint = Point(colIndex, lastRow)
        Pair(firstPoint, lastPoint).let { listOf(it, it.swap()) }
    }.toMap()

    val horizontalWrapPoints = board.flatMapIndexed { rowIndex, row ->
        val firstCol = row.indexOfFirst { it != ' ' }
        val lastCol = row.indexOfLast { it != ' ' }
        val firstPoint = Point(firstCol, rowIndex)
        val lastPoint = Point(lastCol, rowIndex)
        Pair(firstPoint, lastPoint).let { listOf(it, it.swap()) }
    }.toMap()

    fun Point.move(dir: Direction, steps: Int, markers: MutableMap<Point, Direction>): Point {
        var pos = this
        val warpPoints = if (dir.isHorizontal()) horizontalWrapPoints else verticalWrapPoints

        for (step in 0 until steps) {
            val nextPos = pos.move(dir)
            pos = when (board[nextPos]) {
                '#'  -> return pos
                ' '  -> {
                    val warpPos = warpPoints[pos]!!
                    if (board[warpPos] == '#') return pos
                    warpPos
                }
                '.'  -> nextPos
                else -> throw Exception("bad tile")
            }
            markers[pos] = dir
        }
        return pos
    }

    fun Direction.turn(dir: Char): Direction = when (dir) {
        'L' -> turnLeft()
        'R' -> turnRight()
        else -> throw Exception("bad dir $dir")
    }

    fun Direction.toChar() = when (this) {
        Direction.NORTH -> '^'
        Direction.EAST  -> '>'
        Direction.SOUTH -> 'v'
        Direction.WEST  -> '<'
    }

    fun passcode(pos: Point, dir: Direction): Int {
        val facing = (dir.ordinal - 1).mod(4)
        return 1000 * (pos.y + 1) + 4 * (pos.x + 1) + facing
    }

    fun printBoard(markers: Map<Point, Direction>) {
        val str = board.mapIndexed { r, row ->
            (0 until maxCols).map { c ->
                markers[Point(c, r)]?.toChar() ?: row.getOrElse(c) { ' ' }
            }.joinToString("")
        }.joinToString("\n")
        println(str)
    }

    fun part1() {
        var pos = Point(board[0].indexOfFirst { it == '.' }, 0)
        var dir = Direction.EAST
        val markers = mutableMapOf(pos to dir)

        for (action in directions) {
            // println("action: $action")
            if (action[0] == 'L' || action[0] == 'R') {
                dir = dir.turn(action[0])
            } else {
                pos = pos.move(dir, action.toInt(), markers)
            }
            markers[pos] = dir
            // printBoard(markers)
            // println()
        }
        printBoard(markers)
        println()
        println(passcode(pos, dir))
    }

    part1()
}
