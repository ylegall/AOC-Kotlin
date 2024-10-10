package aoc2019

import java.io.File



//private fun totalFuel() = File("input.txt").useLines { lines ->
//    lines.map { fuelForMass(it.toInt()) }.sum()
//}
//
//private fun totalFuelRecursive() = File("inputs/2019/1.txt").useLines { lines ->
//    lines.map { fuelRecursive(it.toInt()) }.sum()
//}

fun main() {

    fun fuelForMass(mass: Int) = (mass / 3 - 2).coerceAtLeast(0)

    fun fuelRecursive(mass: Int): Int {
        val fuel = fuelForMass(mass)
        return fuel + if (fuel > 0) fuelRecursive(fuel) else 0
    }

    val input = File("input.txt").readLines()

    fun part1(input: List<String>): Int {
        return input.sumOf { fuelForMass(it.toInt()) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { fuelRecursive(it.toInt()) }
    }

    println(part1(input))
    println(part2(input))
}