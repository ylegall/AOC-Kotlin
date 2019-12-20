package aoc2019

import util.Direction
import util.Point
import util.enclosingRect
import util.move
import java.util.ArrayDeque

private object Day15 {

    private class RemoteBot(codes: List<Long>) {

        class State(val pos: Point, val steps: Int, val direction: Direction)

        private val map = mutableMapOf(Point(0, 0) to '.')
        private var requestedMove = Direction.NORTH
        private var backTrack = false
        private val stack = ArrayDeque<State>().apply {
            add(State(Point(0, 0), 0, Direction.NORTH))
        }

        private val processor = IntCodeProcessor(
                codes,
                inputSupplier = { getNextMove() },
                outputConsumer = { updateMap(it) }
        )

        private fun pos() = stack.peek().pos

        private fun backTrack(): Long {
            requestedMove = stack.pop().direction.reverse()
            return requestedMove.inputCode()
        }

        fun scanEnvironment(): Map<Point, Char> {
            while (stack.isNotEmpty()) {
                processor.run()
            }
            return map.toMap()
        }

        private fun getOpenNeighbors() = Direction.values().filter {
            pos().move(it) !in map
        }.map {
            it.inputCode()
        }

        private fun getNextMove(): Long {
            val moves = getOpenNeighbors()
            return if (moves.isEmpty()) {
                backTrack = true
                backTrack()
            } else {
                backTrack = false
                moves.first().also { requestedMove = it.decodeDirection() }
            }
        }

        private fun updateMap(code: Long) {

            fun moveConfirmed() {
                val lastState = stack.peek()
                val newState = State(
                        lastState.pos.move(requestedMove),
                        lastState.steps + 1,
                        requestedMove
                )
                stack.push(newState)
                map[newState.pos] = '.'
            }

            if (stack.isEmpty()) {
                processor.pause()
                return
            }

            when (code) {
                0L -> map[pos().move(requestedMove)] = '#'
                1L -> if (!backTrack) moveConfirmed()
                2L -> {
                    processor.pause()
                    moveConfirmed()
                    map[pos()] = '2'
                    println("found goal at ${stack.peek().steps}")
                }
                else -> throw Exception("bad output code: $code")
            }
        }

        private fun Direction.inputCode() = when (this) {
            Direction.NORTH -> 1L
            Direction.SOUTH -> 2L
            Direction.WEST  -> 3L
            Direction.EAST  -> 4L
        }

        private fun Long.decodeDirection() = when(this) {
            1L -> Direction.NORTH
            2L -> Direction.SOUTH
            3L -> Direction.WEST
            4L -> Direction.EAST
            else -> throw Exception("bad direction code: $this")
        }

    }

    fun printMap(map: Map<Point, Char>, seen: Set<Point> = emptySet()) {
        val (minPos, maxPos) = enclosingRect(map.keys)
        val image = (minPos.y .. maxPos.y).joinToString("\n") { y ->
            (minPos.x .. maxPos.x).joinToString("") { x ->
                val point = Point(x, y)
                if (point in seen) "O" else map[Point(x, y)]?.toString() ?: "?"

            }
        }
        println(image)
    }

    private fun minutesToFillMapWithO2(map: Map<Point, Char>): Int {
        val start = map.entries.find { it.value == '2' }!!.key
        val seen = mutableSetOf<Point>()
        var frontier = listOf(start)
        var minutes = 0
        while (frontier.isNotEmpty()) {
            seen.addAll(frontier)
            //printMap(map, seen)
            frontier = frontier.flatMap { it.cardinalNeighbors().filter { map[it] != '#' && it !in seen } }
            minutes++
        }
        return minutes - 1
    }

    fun run() {
        val codes = loadIntCodeInstructions("inputs/2019/15.txt")
        val bot = RemoteBot(codes)
        val map = bot.scanEnvironment()
        //printMap(map)
        println(minutesToFillMapWithO2(map))
    }

}

fun main() {
    Day15.run()
}