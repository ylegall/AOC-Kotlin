package aoc2015

import util.input
import kotlin.streams.toList

private val hexcodeRegex = Regex("""\\x[0-9a-f][0-9a-f]""")

private fun unescape(string: String): String {
    return string.drop(1)
            .dropLast(1)
            .replace("""\"""", """#""")
            .replace("""\\""", """#""")
            .replace(hexcodeRegex, "#")
}

private fun escape(string: String): String {
    return string.replace("""\""", """\\""")
            .replace(""""""", """\"""")
}

fun main() {
    val lines = input("inputs/2015/8.txt").use { it.toList() }

    // part 1
    println(lines.map { it.length - unescape(it).length }.sum())

    // part 2
    println(lines.map { escape(it).length - it.length + 2 }.sum())
}