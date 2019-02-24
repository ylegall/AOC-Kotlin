package aoc2017

import util.input
import kotlin.streams.asSequence


private fun countLinesWithUniqueWords(lines: List<List<String>>) = lines.map { line ->
    line.size == line.toSet().size
}.count { it }

private fun countLinesWithUniqueAnagrams(lines: List<List<String>>) = lines.map { line ->
    line.size == line.map { word -> word.toSortedSet().joinToString("") }.distinct().size
}.count { it }

fun main() {
    val lines = input("inputs/2017/4.txt").use {
        it.asSequence().map { it.split(" ") }.toList()
    }
    println(countLinesWithUniqueWords(lines))
    println(countLinesWithUniqueAnagrams(lines))
}