package aoc2017

import java.io.File


private fun countLinesWithUniqueWords(lines: List<List<String>>) = lines.map { line ->
    line.size == line.toSet().size
}.count { it }

private fun countLinesWithUniqueAnagrams(lines: List<List<String>>) = lines.map { line ->
    line.size == line.map { word -> word.toSortedSet().joinToString("") }.distinct().size
}.count { it }

fun main() {
    val lines = File("inputs/2017/4.txt").useLines { lines ->
        lines.map { it.split(" ") }.toList()
    }
    println(countLinesWithUniqueWords(lines))
    println(countLinesWithUniqueAnagrams(lines))
}

// 383
// 265