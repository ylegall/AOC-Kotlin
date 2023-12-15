package aoc2023

import util.Direction
import util.Point
import util.move
import util.set
import util.get
import util.transpose
import java.io.File

fun main() {

    fun findFirst(start: Point, dir: Direction, input: List<List<Char>>): Point? {
        return generateSequence(start) { pos -> pos.move(dir) }
            .drop(1)
            .takeWhile {it.y in input.indices &&
                    it.x in input[it.y].indices &&
                    input[it] != '#'
            }.firstOrNull { input[it] == 'O' }
    }

    fun List<MutableList<Char>>.tiltNorth() {
        for (row in 0 until this.size-1) {
            for (col in 0 until this[row].size) {
                val pos = Point(col, row)
                val item = this[pos]
                if (item != '.') continue
                val target = findFirst(pos, Direction.SOUTH, this)
                if (target != null) {
                    this[pos] = 'O'
                    this[target] = '.'
                }
            }
        }
    }

    fun MutableList<MutableList<Char>>.tilt(direction: Direction) {
        when (direction) {
            Direction.NORTH -> {
                tiltNorth()
            }
            Direction.EAST -> {
                transpose()
                reverse()
                tiltNorth()
                reverse()
                transpose()
            }
            Direction.SOUTH -> {
                reverse()
                tiltNorth()
                reverse()
            }
            Direction.WEST -> {
                transpose()
                tiltNorth()
                transpose()
            }
        }
    }

    fun totalLoad(input: List<List<Char>>): Int {
        return input.indices.sumOf { row ->
            input[row].indices.sumOf { col ->
                if (input[row][col] == 'O') input.size - row else 0
            }
        }
    }

    fun spinCycle(input: MutableList<MutableList<Char>>, totalCycles: Int = 1) {
        repeat(totalCycles) {
            input.tilt(Direction.NORTH)
            input.tilt(Direction.WEST)
            input.tilt(Direction.SOUTH)
            input.tilt(Direction.EAST)
        }
    }

    fun findLoadAfterCyclesByPattern(input: MutableList<MutableList<Char>>, maxCycles: Int): Int {
        val history = mutableMapOf<Int, Int>()
        for (i in 1 .. maxCycles) {
            spinCycle(input)
            val totalLoad = totalLoad(input)
            // println("$i: $totalLoad")
            if (totalLoad in history) {
                val cycleStart = history[totalLoad]!!
                val cycleLength = i - cycleStart
                val remainder = (maxCycles - cycleStart) % cycleLength
                spinCycle(input, remainder)
                return totalLoad(input)
            }
            history[totalLoad] = i
        }
        return history[maxCycles]!!
    }

    fun List<List<Char>>.print() {
        println(this.joinToString("\n") { it.joinToString("") })
        println()
    }

    fun part1() {
        val input = File("input.txt").readLines().map { it.toMutableList() }.toMutableList()
        input.tiltNorth()
        println(totalLoad(input))
    }

    fun part2() {
        val input = File("input.txt").readLines().map { it.toMutableList() }.toMutableList()
        val result = findLoadAfterCyclesByPattern(input, 1000000000)
        println(result)
    }

    part1()
    part2()

}