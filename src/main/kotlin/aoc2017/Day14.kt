package aoc2017

import util.knotHashToString

private const val INPUT = "amgozmfv"

@ExperimentalStdlibApi
private fun countUsedSquares(): Int {
    return (0 until 128).map { row ->
        val key = "$INPUT-$row".map { it.toInt() }
        val hash = knotHashToString(key)
        hash.map {
            Integer.parseInt(it.toString(), 16).countOneBits()
        }.sum()
    }.sum()
}

@ExperimentalStdlibApi
fun main() {
    println(countUsedSquares())
}
