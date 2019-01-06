package aoc2015

import util.input
import kotlin.streams.toList


private val doubleLetterRegex = Regex("""([a-z])\1""")
private val twoPairsRegex = Regex("""([a-z]{2})[a-z]*\1""")
private val sandwitchRegex = Regex("""([a-z])[a-z]\1""")
private val exludedPairsRegex = Regex("ab|cd|pq|xy")
private val vowels = setOf('a','e','i','o','u')

private fun String.hasDoubleLetter() = doubleLetterRegex.containsMatchIn(this)

private fun String.has3OrMoreVowels() = count { it in vowels } >= 3

private fun String.isNicePart1(): Boolean {
    return has3OrMoreVowels() && hasDoubleLetter() && !exludedPairsRegex.containsMatchIn(this)
}

private fun String.has2PairsOfLetters() = twoPairsRegex.containsMatchIn(this)

private fun String.hasSandwitchLetter() = sandwitchRegex.containsMatchIn(this)

private fun String.isNicePart2(): Boolean {
    return hasSandwitchLetter() && has2PairsOfLetters()
}

fun main() {
    val lines = input("inputs/2015/5.txt").use { it.toList() }
    println(lines.count { it.isNicePart1()} )
    println(lines.count { it.isNicePart2()} )
}
