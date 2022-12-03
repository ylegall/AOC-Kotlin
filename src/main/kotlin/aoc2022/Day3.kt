package aoc2022

import java.io.File

fun main() {

    fun Char.priority() = if (this in 'a'..'z') {
        (this - 'a' + 1)
    } else {
        (this - 'A' + 27)
    }

    fun part1() {
        val result = File("input.txt").useLines { lines ->
            lines.map { line ->
                val items1 = line.substring(0, line.length/2).toSet()
                val items2 = line.substring(line.length/2).toSet()
                val commonItem = items1.intersect(items2).firstOrNull()!!
                commonItem.priority()
            }.sum()
        }
        println(result)
    }

    fun part2() {
        val result = File("input.txt").useLines { lines ->
            lines.chunked(3).map { chunk ->
                chunk.takeLast(2).fold(chunk.first().toSet()) { commonSet, nextChunk ->
                    commonSet.intersect(nextChunk.toSet())
                }.firstOrNull()!!
            }.sumOf { commonItem ->
                commonItem.priority()
            }
        }
        println(result)
    }

    part1()
    part2()
}