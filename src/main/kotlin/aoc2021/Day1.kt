package aoc2021

import java.io.File

fun sonarSweep1(depths: List<Int>): Int {
    return depths.windowed(2).count { it[1] > it[0] }
}

fun sonarSweep2(depths: List<Int>): Int {
    val windowSums = depths.windowed(3).map { it.sum() }
    return windowSums.windowed(2).count { it[1] > it[0] }
}

fun main() {
    val depths = File("inputs/2021/1.txt").useLines { lines -> lines.map { it.toInt() }.toList() }
    println(sonarSweep1(depths))
    println(sonarSweep2(depths))
}