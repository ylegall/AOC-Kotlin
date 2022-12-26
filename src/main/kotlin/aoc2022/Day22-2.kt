package aoc2022

import util.Direction
import util.Point
import util.move
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

    data class Vec(
        val pos: Point,
        val dir: Direction
    )

    fun buildWrapPoints(): Map<Vec, Vec> {
        val map = mutableMapOf<Vec, Vec>()

        val eNorth = (50 until 100).map { col -> Vec(Point(col, 0), Direction.NORTH) }
        val fNorth = (100 until 150).map { col -> Vec(Point(col, 0), Direction.NORTH) }
        val fSouth = (100 until 150).map { col -> Vec(Point(col, 49), Direction.NORTH) }
        val cNorth = (0 until 50).map { col -> Vec(Point(col, 100), Direction.SOUTH) }
        val bSouth = (50 until 100).map { col -> Vec(Point(col, 149), Direction.SOUTH) }
        val dSouth = (0 until 50).map { col -> Vec(Point(col, 199), Direction.NORTH) }

        val eWest = (0 until 50).map { row -> Vec(Point(50, row), Direction.WEST) }
        val fEast = (0 until 50).map { row -> Vec(Point(149, row), Direction.EAST) }
        val aWest = (50 until 100).map { row -> Vec(Point(50, row), Direction.WEST) }
        val aEast = (50 until 100).map { row -> Vec(Point(99, row), Direction.EAST) }
        val cWest = (100 until 150).reversed().map { row -> Vec(Point(0, row), Direction.EAST) }
        val bEast = (100 until 150).reversed().map { row -> Vec(Point(99, row), Direction.WEST) }
        val dWest = (150 until 200).map { row -> Vec(Point(0, row), Direction.EAST) }
        val dEast = (150 until 200).map { row -> Vec(Point(49, row), Direction.WEST) }

        fun generatePairs(a: List<Vec>, b: List<Vec>): List<Pair<Vec, Vec>> {
            val pairs = a.zip(b)
            val reversed = pairs.map { (v1, v2) -> v2.copy(dir = v2.dir.reverse()) to v1.copy(dir = v1.dir.reverse()) }
            return pairs + reversed
        }

        map.putAll(generatePairs(eNorth, dWest))
        map.putAll(generatePairs(fNorth, dSouth))
        map.putAll(generatePairs(fEast, bEast))
        map.putAll(generatePairs(eWest, cWest))
        map.putAll(generatePairs(aWest, cNorth))
        map.putAll(generatePairs(aEast, fSouth))
        map.putAll(generatePairs(bSouth, dEast))

        return map
    }

    val warpPoints = buildWrapPoints()

    fun Point.move(dir: Direction, steps: Int, markers: MutableMap<Point, Direction>): Pair<Point, Direction> {
        var pos = this
        var dir = dir

        for (step in 0 until steps) {
            val nextPos = pos.move(dir)
            when (board[nextPos]) {
                '#'  -> return pos to dir
                ' '  -> {
                    val (newPos, newDir) = warpPoints[Vec(pos, dir)]!!
                    if (board[newPos] == '#') return pos to dir
                    pos = newPos
                    dir = newDir
                }
                '.'  -> {
                    pos = nextPos
                }
                else -> throw Exception("bad tile")
            }
            markers[pos] = dir
        }
        return pos to dir
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

    fun part2() {
        var pos = Point(board[0].indexOfFirst { it == '.' }, 0)
        var dir = Direction.EAST
        val markers = mutableMapOf(pos to dir)

        for (action in directions) {
            if (action[0] == 'L' || action[0] == 'R') {
                dir = dir.turn(action[0])
            } else {
                val vec = pos.move(dir, action.toInt(), markers)
                pos = vec.first
                dir = vec.second
            }
            markers[pos] = dir
            // printBoard(markers)
            // println()
        }
        // printBoard(markers)
        // println()
        println(passcode(pos, dir))
    }

    part2()
}
