package aoc2018

import aoc2018.Day13.Direction.*
import util.Point
import java.io.FileInputStream

private object Day13 {

    private enum class Direction {
        UP, RIGHT, DOWN, LEFT;

        fun turn(turn: TurnType): Direction {
            val newDirection = values().size + this.ordinal + turn.value
            return values()[newDirection % values().size]
        }
    }

    private enum class TurnType(val value: Int) {
        LEFT(-1);

        fun nextTurn(): TurnType {
            return values()[(ordinal + 1) % TurnType.values().size]
        }
    }

    private class Car(
            var pos: Point,
            var direction: Direction,
            var turn: TurnType = TurnType.LEFT
    ) {
        override fun toString() = when(direction) {
            UP    -> "^"
            RIGHT -> ">"
            DOWN  -> "v"
            LEFT  -> "<"
        }

        fun advance() {
            pos = when (direction) {
                UP    -> Point(pos.x, pos.y - 1)
                RIGHT -> Point(pos.x + 1, pos.y)
                LEFT  -> Point(pos.x - 1, pos.y)
                DOWN  -> Point(pos.x, pos.y + 1)
            }
        }

        fun turn(roads: Map<Point, Char>) {
            when (roads[pos] ?: return) {
                '/' -> direction = when (direction) {
                    UP    -> RIGHT
                    LEFT  -> DOWN
                    RIGHT -> UP
                    DOWN  -> LEFT
                }
                '\\' -> direction = when (direction) {
                    UP    -> LEFT
                    LEFT  -> UP
                    RIGHT -> DOWN
                    DOWN  -> RIGHT
                }
                '+' -> {
                    direction = direction.turn(turn)
                    turn = turn.nextTurn()
                }
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

    fun run() {
        val roads = HashMap<Point, Char>()
        val cars = HashMap<Point, Car>()

        FileInputStream("inputs/2018/13.txt").bufferedReader().use {
            var y = 0
            for (line in it.lines()) {
                var x = 0
                for (c in line) {
                    when (c) {
                        '+','\\','/' -> roads[Point(x, y)] = c
                        '>' -> Car(Point(x, y), RIGHT).apply { cars[pos] = this }
                        '^' -> Car(Point(x, y), UP).apply { cars[pos] = this }
                        'v' -> Car(Point(x, y), DOWN).apply { cars[pos] = this }
                        '<' -> Car(Point(x, y), LEFT).apply { cars[pos] = this }
                    }
                    x += 1
                }
                y += 1
            }
            simulate(roads, cars)
        }
    }
}


fun main() {
    Day13.run()
}