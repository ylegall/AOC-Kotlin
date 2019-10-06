package aoc2018

import aoc2018.Day17.CellType.*
import util.Point
import util.input
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.asSequence


private object Day17 {

    private enum class CellType {
        BLOCK,
        RUNNING,
        LEFT_BLOCKED,
        RIGHT_BLOCKED,
        STILL,
        EMPTY
    }

    private val inputRegex = Regex("""([xy])=(\d+), ([xy])=(\d+)..(\d+)""")
    private val startCell = Point(500, 0)

    private fun parseInputLine(line: String): Iterable<Point> {
        val match = inputRegex.matchEntire(line) ?: throw IllegalStateException("bad input line: $line")
        val (staticAxis, length, _, start, end) = match.destructured
        val range = IntRange(start.toInt(), end.toInt())
        return range.map { value ->
            if (staticAxis == "y") {
                Point(value, length.toInt())
            } else {
                Point(length.toInt(), value)
            }
        }
    }

    private class Grid(lines: Sequence<String>) {

        private var minY: Int = Int.MAX_VALUE
        private var minX: Int = 500
        private var maxX: Int = 500
        private var maxY: Int = Int.MIN_VALUE
        private val cells = HashMap<Point, CellType>()

        init {
            lines.forEach { line ->
                addBlocks(parseInputLine(line))
            }
            println("minY=$minY, maxY=$maxY")
        }

        private fun addBlocks(cells: Iterable<Point>) {
            cells.forEach { point ->
                minX = min(minX, point.x - 1)
                maxX = max(maxX, point.x + 1)
                maxY = max(maxY, point.y)
                minY = min(minY, point.y)
                this.cells[point] = BLOCK
            }
        }

        fun countWaterCells(): Int {
            val waterFrontier = ArrayDeque<Point>()
            startCell.setType(RUNNING)
            waterFrontier.push(startCell)

            fun ArrayDeque<Point>.push(vararg points: Point) {
                for (point in points) { this.push(point) }
            }

            while (waterFrontier.isNotEmpty()) {
                val cell = waterFrontier.pop()
                val bottomNeighbor = cell.bottomNeighbor()
                val topNeighbor = cell.topNeighbor()
                val leftNeighbor = cell.leftNeighbor()
                val rightNeighbor = cell.rightNeighbor()

                if (bottomNeighbor.y > maxY) {
                    continue
                }

                when (val type = cell.type()) {
                    RUNNING -> {
                        when {
                            bottomNeighbor.isOpen() -> {
                                bottomNeighbor.setType(RUNNING)
                                waterFrontier.push(bottomNeighbor)
                            }
                            bottomNeighbor.isFilled() -> when {
                                leftNeighbor.isOpen() && rightNeighbor.isOpen() -> {
                                    leftNeighbor.setType(RUNNING)
                                    rightNeighbor.setType(RUNNING)
                                    waterFrontier.push(rightNeighbor, leftNeighbor)
                                }
                                leftNeighbor.isOpen() -> {
                                    leftNeighbor.setType(RUNNING)
                                    waterFrontier.push(leftNeighbor)
                                }
                                rightNeighbor.isOpen() -> {
                                    rightNeighbor.setType(RUNNING)
                                    waterFrontier.push(rightNeighbor)
                                }
                                leftNeighbor.isLeftBlocked() -> when {
                                    rightNeighbor.isFilled() -> {
                                        cell.setType(STILL)
                                        if (!topNeighbor.isOpen()) waterFrontier.push(topNeighbor)
                                        waterFrontier.push(leftNeighbor)
                                    }
                                    rightNeighbor.isRightBlocked() -> {
                                        cell.setType(STILL)
                                        if (!topNeighbor.isOpen()) waterFrontier.push(topNeighbor)
                                        waterFrontier.push(leftNeighbor, rightNeighbor)
                                    }
                                    else -> {
                                        cell.setType(LEFT_BLOCKED)
                                        waterFrontier.push(rightNeighbor)
                                    }
                                }
                                rightNeighbor.isRightBlocked() -> when {
                                    leftNeighbor.isFilled() -> {
                                        cell.setType(STILL)
                                        if (!topNeighbor.isOpen()) waterFrontier.push(topNeighbor)
                                        waterFrontier.push(rightNeighbor)
                                    }
                                    else -> {
                                        cell.setType(RIGHT_BLOCKED)
                                        waterFrontier.push(leftNeighbor)
                                    }
                                }
                            }
                        }
                    }
                    LEFT_BLOCKED -> when {
                        rightNeighbor.isFilled() -> {
                            cell.setType(STILL)
                            if (!topNeighbor.isOpen()) waterFrontier.push(topNeighbor)
                            waterFrontier.push(leftNeighbor)
                        }
                    }
                    RIGHT_BLOCKED -> when {
                        leftNeighbor.isFilled() -> {
                            cell.setType(STILL)
                            if (!topNeighbor.isOpen()) waterFrontier.push(topNeighbor)
                            waterFrontier.push(rightNeighbor)
                        }
                    }
                    STILL, EMPTY, BLOCK -> {}
                    else -> throw Exception("invalid cell type: $type")
                }

                // debug
                //printGrid()
                //System.`in`.read()
                //sleep(200)
            }

            return (minY .. maxY).flatMap { y ->
                (minX .. maxX).map { x ->
                    Point(x, y)
                }
            }.count { it.isWater() }
        }

        private operator fun get(point: Point): CellType = cells[point] ?: EMPTY

        private fun Point.setType(type: CellType) {
            cells[this] = type
        }

        private fun Point.type() = get(this)

        private fun Point.isOpen() = get(this) == EMPTY

        private fun Point.isRightBlocked() = get(this).let {
            it == STILL || it == BLOCK || it == RIGHT_BLOCKED
        }

        private fun Point.isLeftBlocked() = get(this).let {
            it == STILL || it == BLOCK || it == LEFT_BLOCKED
        }

        private fun Point.isFilled() = get(this).let { it == STILL || it == BLOCK }

        private fun Point.isWater() = get(this).let { it != EMPTY && it != BLOCK }

        private fun Point.topNeighbor() = Point(x, y - 1)
        private fun Point.rightNeighbor() = Point(x + 1, y)
        private fun Point.bottomNeighbor() = Point(x, y + 1)
        private fun Point.leftNeighbor() = Point(x - 1, y)

        fun printGridToFile(name: String = "grid.txt") {
            FileOutputStream(name).use { printGrid(PrintStream(it)) }
        }

        fun printGrid(output: PrintStream = System.`out`) {
            for (y in 0 .. maxY) {
                val row = (minX .. maxX).map { x ->
                    when (this[Point(x, y)]) {
                        BLOCK         -> '#'
                        STILL         -> '~'
                        RIGHT_BLOCKED -> '<'
                        LEFT_BLOCKED  -> '>'
                        RUNNING       -> '|'
                        EMPTY         -> '·'
                    }
                }.joinToString("")
                output.println(row)
            }
        }
    }

    fun run() {
        val grid = input("inputs/2018/17.txt").use { Grid(it.asSequence()) }
        val waterCells = grid.countWaterCells()
        //grid.printGrid()
        println("filled $waterCells cells")
    }
}

fun main() {
    Day17.run()
}