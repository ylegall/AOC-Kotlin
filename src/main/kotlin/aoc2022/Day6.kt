package aoc2022

import java.io.File

fun main() {

    val input = File("input.txt").readText()

    fun firstIndexWithUniqueCount(count: Int): Int? {
        return input.asSequence()
            .windowed(count)
            .mapIndexed { index, chars ->
                (index + count) to chars.toSet().size
            }
            .find {
                it.second == count
            }?.first
    }

    fun part1() {
        println(firstIndexWithUniqueCount(4))
    }

    fun part2() {
        println(firstIndexWithUniqueCount(14))
    }

    part1()
    part2()
}