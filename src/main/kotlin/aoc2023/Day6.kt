package aoc2023

import util.product
import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt


fun main() {

    fun parseInput(filename: String): Pair<List<Long>, List<Long>> = File(filename).readLines().map { line ->
        line.split("""\W+""".toRegex()).drop(1).map { it.toLong() }
    }.let {
        Pair(it.first(), it.last())
    }

    fun roots(a: Long, b: Long, c: Long): List<Double> {
        val disc = sqrt(b * b - 4.0 * a * c)
        val den = 2 * a
        return listOf(
                (-b + disc) / den,
                (-b - disc) / den
        )
    }

    // solve quadratic equation: dist = -t*t + timeLimit * t - recordDist
    fun numberOfWaysToWinRace(timeLimit: Long, recordDist: Long): Long {
        val roots = roots(-1, timeLimit, -recordDist)
        // println("timeLimit=$timeLimit, recordDist=$recordDist, roots=$roots")
        val low = floor(roots[0] + 1)
        val high = ceil(roots[1] - 1)
        return (high - low + 1).toLong()
    }

    fun part1() {
        val (times, distances) = parseInput("input.txt")
        val result = times.indices.map { i ->
            numberOfWaysToWinRace(times[i], distances[i])
        }.product(1)
        println(result)
    }

    fun part2() {
        val (times, distances) = parseInput("input.txt")
        val time = times.joinToString("").toLong()
        val dist = distances.joinToString("").toLong()
        println(numberOfWaysToWinRace(time, dist))
    }

    part1()
    part2()
}