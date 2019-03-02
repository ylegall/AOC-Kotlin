package aoc2015

import util.input
import java.util.stream.Stream
import kotlin.streams.asSequence

private val featuresToMatch = mapOf(
    "children"    to "3",
    "cats"        to "7",
    "samoyeds"    to "2",
    "pomeranians" to "3",
    "akitas"      to "0",
    "vizslas"     to "0",
    "goldfish"    to "5",
    "trees"       to "3",
    "cars"        to "2",
    "perfumes"    to "1"
)

private fun Map<String, String>.isPotentialMatch() = entries.all { feature ->
    featuresToMatch[feature.key] == feature.value
}

private fun findAuntSue1(lines: Stream<String>) {
    lines.asSequence().mapIndexed { idx, line ->
        val featureMap = line.split(": ", ", ").drop(1).chunked(2).associate { it[0] to it[1] }
        Pair(idx + 1, featureMap)
    }.filter {
        it.second.isPotentialMatch()
    }.toList().forEach { println(it) }
}

private fun Map<String, String>.isPotentialMatch2() = entries.all { feature ->
    when (feature.key) {
        "cats", "trees" -> featuresToMatch[feature.key]!!.toInt() < feature.value.toInt()
        "pomeranians", "goldfish" -> featuresToMatch[feature.key]!!.toInt() > feature.value.toInt()
        else -> featuresToMatch[feature.key] == feature.value
    }
}

private fun findAuntSue2(lines: Stream<String>) {
    lines.asSequence().mapIndexed { idx, line ->
        val featureMap = line.split(": ", ", ").drop(1).chunked(2).associate { it[0] to it[1] }
        Pair(idx + 1, featureMap)
    }.filter {
        it.second.isPotentialMatch2()
    }.toList().forEach { println(it) }
}

fun main() {
    input("inputs/2015/16.txt").use(::findAuntSue1)
    input("inputs/2015/16.txt").use(::findAuntSue2)
}