package aoc2016

import util.input
import kotlin.streams.asSequence

private const val DOOR_KEY = "northpole"
private val doorPattern = Regex("""([a-z\-]+)(\d+)\[([a-z]+)]""")

private fun top5Letters(name: String): String {
    return name.filterNot {
        it == '-'
    }.groupingBy {
        it
    }.eachCount().entries.sortedWith(
        compareBy({ -it.value }, { it.key })
    ).take(5).map {
        it.key
    }.joinToString("")
}

private fun Char.rotateBy(amount: Int): Char {
    return 'a' + (((this - 'a') + amount) % 26)
}

private fun String.rotateBy(amount: Int) = this.map {
    if (it != '-') it.rotateBy(amount) else ' '
}.joinToString("")

private fun sectorIdSum(lines: Sequence<String>): Int {
    return lines.mapNotNull { line ->
        doorPattern.matchEntire(line)?.destructured?.let { (name, sectorId, checksum) ->
            if (top5Letters(name) == checksum) sectorId.toInt() else null
        }
    }.sum()
}

private fun findSectorIdDecryped(lines: Sequence<String>): String {
    return lines.map { line ->
        doorPattern.matchEntire(line)?.destructured?.let { (name, sectorId, _) ->
            Pair(name.rotateBy(sectorId.toInt()), sectorId)
        }
    }.find {
        it?.first?.contains(DOOR_KEY, ignoreCase = true) ?: false
    }?.second ?: throw Exception("not found")
}

fun main() {
    val checksum = input("inputs/2016/4.txt").use { lines ->
        sectorIdSum(lines.asSequence())
    }
    println(checksum)

    val sectorId = input("inputs/2016/4.txt").use { lines ->
        findSectorIdDecryped(lines.asSequence())
    }
    println(sectorId)
}