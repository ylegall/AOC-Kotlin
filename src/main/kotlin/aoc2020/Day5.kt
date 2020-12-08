package aoc2020

import java.io.File

private object Day5 {

    fun decodeRow(code: String): Int {
        return code.replace('F','0')
                .replace('B', '1')
                .toInt(2)
    }

    fun decodeCol(code: String): Int {
        return code.replace('L','0')
                .replace('R', '1')
                .toInt(2)
    }

    fun decode(code: String): Pair<Int, Int> {
        val row = decodeRow(code.take(7))
        val col = decodeCol(code.drop(7))
        return row to col
    }

    fun getSeatId(code: String): Int {
        val (row, col) = decode(code)
        return 8 * row + col
    }
}

fun main() {
    val lines = File("inputs/2020/5.txt").readLines()
    val ids = lines.map { it to Day5.getSeatId(it) }.sortedBy { it.second }
    println(ids.last())

    for (i in 0 until ids.size - 1) {
        val (_, number) = ids[i]
        val (_, number1) = ids[i + 1]
        if (number1 != number + 1) {
            println(number + 1)
            break
        }
    }
}