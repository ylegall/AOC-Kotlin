package aoc2016

import util.input
import kotlin.streams.asSequence

private val markerPattern = Regex("""\((\d+)x(\d+)\)""")

private fun String.decompressedLength(processExpansion: Boolean = false): Long {
    val match = markerPattern.find(this)
    return if (match == null) {
        this.length.toLong()
    } else {
        val markerRange = match.groups[0]!!.range
        val len = match.groups[1]?.value?.toInt()!!
        val repeat = match.groups[2]?.value?.toInt()!!
        val markerEnd = markerRange.last + 1
        return markerRange.start + if (!processExpansion) {
            (len * repeat) + substring(markerEnd + len).decompressedLength()
        } else {
            substring(markerEnd, markerEnd + len).decompressedLength(processExpansion) * repeat +
            substring(markerEnd + len).decompressedLength(processExpansion)
        }
    }
}

fun main() {
    val file = input("inputs/2016/9.txt").use { it.asSequence().joinToString() }

    println(file.decompressedLength())
    println(file.decompressedLength(processExpansion = true))
}