package aoc2025

import util.split
import java.io.File

private object Day12 {
    data class Region(
        val width: Int,
        val height: Int,
        val shapeCounts: List<Int>
    ) {
        fun area() = width * height
    }

    class Input(
        val shapes: List<List<String>>,
        val regions: List<Region>
    )

    fun parseInput(): Input {
        val lines = File("input.txt").readLines()
        val parts = lines.split { it.isEmpty() }
        val shapes = parts.dropLast(1).map { chunk ->
            chunk.drop(1)
        }
        val regions = parts.last().map { line ->
            val tokens = line.split('x',':',' ').filter { it.isNotEmpty() }
            Region(tokens[0].toInt(), tokens[1].toInt(), tokens.drop(2).map { it.toInt() })
        }
        return Input(shapes, regions)
    }

    fun part1(input: Input) {
        val shapeAreas = input.shapes.map { shape -> shape.sumOf { line -> line.count { it == '#' } } }
        val validRegions = input.regions.count { region ->
            val requiredShapeArea = region.shapeCounts.indices.sumOf { i -> region.shapeCounts[i] * shapeAreas[i] }
            requiredShapeArea <= region.area()
        }
        println(validRegions)
    }
}

fun main() {
    val input = Day12.parseInput()
    Day12.part1(input)
}
