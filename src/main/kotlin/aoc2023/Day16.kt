package aoc2023

import util.*
import util.Direction.*
import java.io.File

fun main() {

    data class Beam(
        val pos: Point,
        val dir: Direction
    ) {

        fun step(tile: Char): List<Beam> {

            fun head(direction: Direction) = Beam(pos.move(direction), direction)

            return when (tile) {
                '.' -> listOf(head(dir))
                '-' -> when (dir) {
                    NORTH, SOUTH -> listOf(head(WEST), head(EAST))
                    EAST, WEST -> listOf(head(dir))
                }
                '|' -> when (dir) {
                    EAST, WEST -> listOf(head(NORTH), head(SOUTH))
                    NORTH, SOUTH -> listOf(head(dir))
                }
                '/' -> when (dir) {
                    NORTH -> listOf(head(EAST))
                    EAST -> listOf(head(NORTH))
                    SOUTH -> listOf(head(WEST))
                    WEST -> listOf(head(SOUTH))
                }
                '\\' -> when (dir) {
                    NORTH -> listOf(head(WEST))
                    EAST -> listOf(head(SOUTH))
                    SOUTH -> listOf(head(EAST))
                    WEST -> listOf(head(NORTH))
                }
                else -> throw Exception("bad tile $tile")
            }
        }
    }

    fun List<String>.printRegion(beamPoints: Set<Point>) {
        for (r in this.indices) {
            println((0 until this[r].length).joinToString("") { c ->
                if (Point(c, r) in beamPoints) "#" else "."
            })
        }
        println()
    }

    fun traceBeams(input: List<String>, startBeam: Beam): Set<Beam> {
        val seen = mutableSetOf(startBeam)
        val bounds = input.bounds()
        val q = arrayDequeOf(startBeam)

        while (q.isNotEmpty()) {
            val beam = q.removeFirst()
            val tile = input[beam.pos]
            val newBeams = beam.step(tile)
                .filter { it.pos in bounds }

            for (newBeam in newBeams) {
                if (newBeam !in seen) {
                    seen.add(newBeam)
                    q.add(newBeam)
                }
            }
        }
        return seen
    }

    fun part1() {
        val input = File("input.txt").readLines()
        traceBeams(input, Beam(Point(0, 0), EAST))
            .map { it.pos }
            .toSet()
            .also { println(it.size) }
    }

    fun part2() {
        val input = File("input.txt").readLines()
        val startingBeams = input.indices.map { Beam(Point(0, it), EAST) } +
                input.indices.map { Beam(Point(input[0].lastIndex, it), WEST) } +
                input[0].indices.map { Beam(Point(it, 0), SOUTH) } +
                input[0].indices.map { Beam(Point(it, input.lastIndex), NORTH) }
        val maxTiles = startingBeams.maxOf { startBeam ->
            traceBeams(input, startBeam).map { it.pos }.distinct().size
        }
        println(maxTiles)
    }

    part1()
    part2()
}
