package aoc2019

import java.io.File

private fun fuelForMass(mass: Int) = (mass / 3 - 2).coerceAtLeast(0)

private fun fuelRecursive(mass: Int): Int {
    val fuel = fuelForMass(mass)
    return fuel + if (fuel > 0) fuelRecursive(fuel) else 0
}

private fun totalFuel() = File("inputs/2019/1.txt").useLines { lines ->
    lines.map { fuelForMass(it.toInt()) }.sum()
}

private fun totalFuelRecursive() = File("inputs/2019/1.txt").useLines { lines ->
    lines.map { fuelRecursive(it.toInt()) }.sum()
}

fun main() {
    println(totalFuel())
    println(totalFuelRecursive())
}