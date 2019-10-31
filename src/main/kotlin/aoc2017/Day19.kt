package aoc2017

import util.Direction
import util.Direction.*
import util.Point
import util.move
import java.io.File

private object Day19 {

    private fun List<String>.charAt(point: Point) = getOrNull(point.y)?.getOrNull(point.x) ?: ' '

    fun findLettersOnPath(grid: List<String>) {
        var steps = 1
        var position = Point(grid[0].indexOf('|'), 0)
        var direction = SOUTH
        val seenLetters = arrayListOf<Char>()

        while (true) {
            val nextPosition = position.move(direction)

            val nextDirection = when(val nextChar = grid.charAt(nextPosition)) {
                '+' -> {
                    val neighbors = Direction.values().map { dir ->
                        dir to grid.charAt(nextPosition.move(dir))
                    }.toMap()

                    when (direction) {
                        NORTH, SOUTH -> if (neighbors[EAST] != ' ') EAST else WEST
                        EAST, WEST   -> if (neighbors[NORTH] != ' ') NORTH else SOUTH
                    }
                }
                in 'A'..'Z' -> {
                    seenLetters.add(nextChar)
                    direction
                }
                ' ' -> null
                else -> direction
            } ?: break

            position = nextPosition
            direction = nextDirection
            steps++
        }
        println(seenLetters.joinToString(""))
        println(steps)
    }
}


fun main() {
    val input = File("inputs/2017/19.txt").readLines()
    Day19.findLettersOnPath(input)
}