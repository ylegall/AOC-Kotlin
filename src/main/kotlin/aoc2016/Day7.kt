package aoc2016

import util.input
import java.util.stream.Stream
import kotlin.streams.asSequence

private const val brks = """[\[\]]"""
private val bracketPattern = Regex("""\[([a-z]+)]""")
private val abbaPattern = Regex("""([a-z])([a-z])\2\1""")
private val aaaaPattern = Regex("""([a-z])\1\1\1""")
private val abaBabPattern = Regex("""((\w)(\w)\2)\w*$brks(\w*$brks\w*$brks)*\w*\3\2\3""")
private val aaaPattern = Regex("""([a-z])\1\1""")

private fun String.isValid() = abbaPattern.findAll(this)
        .filterNot { match -> aaaaPattern.matches(match.value) }
        .any()

private fun String.isInvalid() = bracketPattern.findAll(this)
        .filter { match -> match.value.isValid() }
        .any()

private fun countTLS(lines: Stream<String>) = lines.asSequence().filter {
    !it.isInvalid()
}.filter {
    it.isValid()
}.count()

private fun countSSL(lines: Stream<String>) = lines.asSequence().filter {
    abaBabPattern.findAll(it).filterNot { match ->
        aaaPattern.matches(match.groupValues[1])
    }.any()
}.count()

fun main() {
    println(input("inputs/2016/7.txt").use(::countTLS))
    println(input("inputs/2016/7.txt").use(::countSSL))
}

