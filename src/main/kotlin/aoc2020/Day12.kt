package aoc2020

import util.Direction
import util.Point
import java.io.File
import kotlin.math.abs


private object Day12 {

    private fun Point.rotate90(cw: Boolean = true) = if (cw) {
        Point(y, -x)
    } else {
        Point(-y, x)
    }

    class Ship {
        var heading = Direction.EAST
        var x = 0
        var y = 0
        var waypoint = Point(0, 0)

        fun update(action: Char, units: Int) {
            when (action) {
                'L' -> {
                    repeat (units / 90) {
                        heading = heading.turnLeft()
                    }
                }
                'R' -> {
                    repeat (units / 90) {
                        heading = heading.turnRight()
                    }
                }
                'F' -> {
                    when (heading) {
                        Direction.NORTH -> y -= units
                        Direction.EAST  -> x += units
                        Direction.SOUTH -> y += units
                        Direction.WEST  -> x -= units
                    }
                }
                'N' -> y -= units
                'S' -> y += units
                'E' -> x += units
                'W' -> x -= units
                else -> throw Exception("invalid action: $action")
            }
        }

        fun updateWithWayPoint(action: Char, units: Int) {
            when (action) {
                'N' -> waypoint += (Point(0, 1) * units)
                'S' -> waypoint -= (Point(0, 1) * units)
                'E' -> waypoint += (Point(1, 0) * units)
                'W' -> waypoint -= (Point(1, 0) * units)
                'F' -> {
                    val delta = (waypoint) * units
                    x += delta.x
                    y += delta.y
                }
                'L' -> {
                    repeat(units / 90) {
                        waypoint = waypoint.rotate90(false)
                    }
                }
                'R' -> {
                    repeat(units / 90) {
                        waypoint = waypoint.rotate90(true)
                    }
                }
            }
        }
    }

    fun findManhattanDistance(actions: List<Pair<Char, Int>>): Int {
        val ship = Ship()
        for ((action, units) in actions) {
            ship.update(action, units)
        }
        return abs(ship.x) + abs(ship.y)
    }

    fun findManhattanDistanceWaypoint(actions: List<Pair<Char, Int>>): Int {
        val ship = Ship()
        ship.waypoint = Point(10, 1)

        for ((action, units) in actions) {
            ship.updateWithWayPoint(action, units)
        }
        return abs(ship.x) + abs(ship.y)
    }
}

fun main() {
    val input = File("inputs/2020/12.txt").readLines().map { line ->
        val action = line[0]
        val units = line.drop(1).toInt()
        action to units
    }
    println(Day12.findManhattanDistance(input))
    println(Day12.findManhattanDistanceWaypoint(input))
}