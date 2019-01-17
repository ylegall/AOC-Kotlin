package aoc2016

import util.Point
import util.input
import util.mDist
import util.plus
import util.times
import kotlin.streams.toList

private val directions = arrayOf(
    Point(0, -1),
    Point(1, 0),
    Point(0, 1),
    Point(-1, 0)
)

private fun Point.move(start: Point, mag: Int) = (this * mag) + start

private class State(
        val pos: Point,
        val dir: Point,
        val visited: Set<Point> = setOf(),
        val endPoint: Point? = null
)

private fun Point.turn(c: Char) = when(c) {
    'L' -> -1
    'R' -> 1
    else -> throw Exception("you might be going in a bad direction")
}.let {
    directions[(directions.size + directions.indexOf(this) + it) % directions.size]
}

private fun State.moveBy(nextMove: String): State {
    val newDirection = dir.turn(nextMove.first())
    val magnitude = nextMove.drop(1).toInt()
    val newPosition = newDirection.move(pos, magnitude)
    val newVisited = expandPoints(this.pos, newPosition, newDirection)
    return State(
            newPosition,
            newDirection,
            visited + newVisited,
            endPoint ?: newVisited.asSequence().find(visited::contains)
    )
}

private fun expandPoints(start: Point, end: Point, direction: Point): List<Point> {
    return generateSequence(start) {
        it + direction
    }.takeWhile {
        it != end
    }.toList()
}

private fun distanceFromHQ(moves: List<String>) = moves.fold(
        State(Point(0, 0), directions[0]),
        State::moveBy
)

fun main() {
    val directions = input("inputs/2016/1.txt").use {
        it.toList().first().split(", ")
    }
    val state = distanceFromHQ(directions)
    println(state.pos.mDist(Point(0,0)))
    println(state.endPoint!!.mDist(Point(0,0)))
}
