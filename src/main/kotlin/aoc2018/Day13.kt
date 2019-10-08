package aoc2018

import util.Point
import java.io.FileInputStream

private enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

private enum class TurnType(val value: Int) {
    LEFT(-1)
}

private fun TurnType.nextTurn(): TurnType {
    return TurnType.values()[(ordinal + 1) % TurnType.values().size]
}

private fun Direction.turn(turn: TurnType): Direction {
    val newDirection = Direction.values().size + this.ordinal + turn.value
    return Direction.values()[newDirection % Direction.values().size]
}

private class Car(
        var pos: Point,
        var direction: Direction,
        var turn: TurnType = TurnType.LEFT
) {
    override fun toString() = when(direction) {
        Direction.UP    -> "^"
        Direction.RIGHT -> ">"
        Direction.DOWN  -> "v"
        Direction.LEFT  -> "<"
    }
}

private fun Car.advance() {
    pos = when (direction) {
        Direction.UP -> Point(pos.x, pos.y - 1)
        Direction.RIGHT -> Point(pos.x + 1, pos.y)
        Direction.LEFT -> Point(pos.x - 1, pos.y)
        Direction.DOWN -> Point(pos.x, pos.y + 1)
    }
}

private fun Car.turn(roads: Map<Point, Char>) {
    when (roads[pos] ?: return) {
        '/' -> direction = when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.LEFT -> Direction.DOWN
            Direction.RIGHT -> Direction.UP
            Direction.DOWN -> Direction.LEFT
        }
        '\\' -> direction = when (direction) {
            Direction.UP -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.RIGHT
        }
        '+' -> {
            direction = direction.turn(turn)
            turn = turn.nextTurn()
        }
    }
}

private fun simulate(roads: Map<Point, Char>, cars: HashMap<Point, Car>) {
    if (cars.isEmpty()) return
    while (cars.size > 1) {
        val sortedCars = cars.values.sortedWith(compareBy({ it.pos.y }, { it.pos.x }))
        for (car in sortedCars) {
            cars.remove(car.pos) ?: continue
            car.advance()
            if (car.pos in cars) {
                println("crash at ${car.pos}")
                cars.remove(car.pos)
            } else {
                cars[car.pos] = car
                car.turn(roads)
            }
        }
    }
    println("last car at ${cars.values.first().pos}")
}

fun main() {
    val roads = HashMap<Point, Char>()
    val cars = HashMap<Point, Car>()

    FileInputStream("inputs/2018/13.txt").bufferedReader().use {
        var y = 0
        for (line in it.lines()) {
            var x = 0
            for (c in line) {
                when (c) {
                    '+','\\','/' -> roads[Point(x, y)] = c
                    '>' -> Car(Point(x, y), Direction.RIGHT).apply { cars[pos] = this }
                    '^' -> Car(Point(x, y), Direction.UP).apply { cars[pos] = this }
                    'v' -> Car(Point(x, y), Direction.DOWN).apply { cars[pos] = this }
                    '<' -> Car(Point(x, y), Direction.LEFT).apply { cars[pos] = this }
                }
                x += 1
            }
            y += 1
        }
        simulate(roads, cars)
    }

}