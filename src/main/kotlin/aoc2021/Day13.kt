package aoc2021

import util.Point
import java.io.File

fun main() {

    data class Fold(
        val axis: Char,
        val position: Int
    )

    class Input(
        val points: List<Point>,
        val folds: List<Fold>,
    )

    fun Point.fold(fold: Fold): Point {
        return Point(
            if (fold.axis == 'x' && x > fold.position) 2 * fold.position - x else x,
            if (fold.axis == 'y' && y > fold.position) 2 * fold.position - y else y
        )
    }

    fun part1(input: Input): Int {
        return input.points.map {
            it.fold(input.folds.first())
        }.toSet().size
    }

    fun part2(input: Input): String {
        var maxX = -1
        var maxY = -1
        var points = input.points.toSet()

        for (fold in input.folds) {
            if (fold.axis == 'x') maxX = fold.position else maxY = fold.position
            points = points.map { it.fold(fold) }.toSet()
        }

        return (0 until maxY).joinToString("\n") { row ->
            (0 until maxX).joinToString("") { col ->
                if (Point(col, row) in points) "#" else "."
            }
        }
    }

    val lines = File("inputs/2021/input.txt").readLines()

    val points = lines.takeWhile { it != "" }.map { line ->
        line.split(",").let { tokens ->
            Point(tokens[0].toInt(), tokens[1].toInt())
        }
    }

    val folds = lines.drop(points.size + 1).map { line ->
        line.split("=").let { tokens ->
            Fold(tokens[0].last(), tokens[1].toInt())
        }
    }

    val input = Input(points, folds)

    println(part1(input))
    println(part2(input))
}