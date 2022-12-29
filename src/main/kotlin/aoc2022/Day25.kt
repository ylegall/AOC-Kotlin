package aoc2022

import java.io.File

fun main() {

    fun toDecimal(str: String): Long {
        return str.fold(0L) { sum, c ->
            sum * 5 + when (c) {
                '=' -> -2
                '-' -> -1
                else -> c.digitToInt()
            }
        }
    }

    fun toSnafu(decimal: Long): String {
        val result = StringBuilder()
        var remaining = decimal
        while (remaining > 0) {
            when (val rem = remaining % 5) {
                in 0 .. 2 -> {
                    result.append(rem.toString())
                }
                3L -> {
                    remaining += 5
                    result.append("=")
                }
                4L -> {
                    remaining += 5
                    result.append("-")
                }
            }
            remaining /= 5
        }
        return result.toString().reversed()
    }

    val input = File("input.txt").readLines()

    fun part1() {
        println(toSnafu(input.sumOf { toDecimal(it) }))
    }

    part1()
}
