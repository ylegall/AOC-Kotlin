package aoc2017

import util.input
import kotlin.streams.asSequence

private fun isScanned(pos: Int, scanners: Map<Int, Int>, delay: Int = 0): Boolean {
    val t = pos + delay
    val scannerSize = scanners[pos] ?: return false
    return (t % (scannerSize * 2 - 2)) == 0
}

private fun findTotalSeverity(scanners: Map<Int, Int>, delay: Int = 0 ): Int {
    val limit = scanners.maxBy { it.key }!!.key
    return (0 .. limit).filter {
        isScanned(it, scanners, delay)
    }.map {
        it * scanners.getOrDefault(it, 0)
    }.sum()//.also { println("delay=$delay, $it") }
}

private fun parseInput(): Map<Int, Int> {
    return input("inputs/2017/13.txt").use {
        it.asSequence().map { line ->
            val tokens = line.split(": ")
            tokens[0].toInt() to tokens[1].toInt()
        }.toMap()
    }
}

private fun noCatch(scanners: Map<Int, Int>) : Int {
    return generateSequence(1) { num ->
        num + 1
    }.map { delay ->
        val isCaught = scanners.keys.map { isScanned(it, scanners, delay) }.any { it }
        isCaught to delay
    }.filterNot {
        it.first
    }.first().second
}

fun main() {
    println(findTotalSeverity(parseInput()))

    println(noCatch(parseInput()))
}