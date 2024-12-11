package aoc2024

import util.Point
import util.get
import util.getOrNull
import java.io.File

fun main() {
    val grid = File("input.txt").readLines()

    data class Path(
        val points: List<Point>
    )

    fun findPaths(
        start: Point,
        grid: List<String>,
        onlyConsiderEndpoints: Boolean
    ): List<Path> {
        val paths = mutableListOf<Path>()
        val seen = mutableSetOf<Pair<Point, Point>>()
        val q = ArrayDeque<Path>()
        q.add(Path(listOf(start)))

        while (q.isNotEmpty()) {
            val path = q.removeFirst()
            val currentPoint = path.points.last()
            val currentHeight = grid[currentPoint]
            if (currentHeight == '9') {
                if (onlyConsiderEndpoints) {
                    val endpoints = path.points.first() to path.points.last()
                    if (endpoints !in seen) {
                        paths.add(path)
                        seen.add(endpoints)
                    }
                } else {
                    paths.add(path)
                }
            } else {
                val newPaths = currentPoint.cardinalNeighbors()
                    .filter { pos -> (grid.getOrNull(pos) ?: '.') == (currentHeight + 1) }
                    .map { Path(path.points + it) }
                q.addAll(newPaths)
            }
        }
        return paths
    }

    fun score(paths: List<Path>): Int {
        return paths.groupBy { it.points.first() }
            .values.sumOf { it.size }
    }

    fun part1() {
        val startPoints = grid.indices.flatMap { row ->
            grid[row].indices.map { col -> Point(col, row) }
                .filter { grid[it] == '0' }
        }
        val validPaths = startPoints.flatMap { findPaths(it, grid, onlyConsiderEndpoints = true) }
        println(score(validPaths))

        val validPaths2 = startPoints.flatMap { findPaths(it, grid, onlyConsiderEndpoints = false) }
        println(score(validPaths2))
    }

    part1()
}