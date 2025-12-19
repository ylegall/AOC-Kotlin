package aoc2015

import java.io.File

fun main() {

    val doubleLetterRegex = Regex("""([a-z])\1""")
    val twoPairsRegex = Regex("""([a-z]{2})[a-z]*\1""")
    val sandwitchRegex = Regex("""([a-z])[a-z]\1""")
    val exludedPairsRegex = Regex("ab|cd|pq|xy")
    val vowels = setOf('a','e','i','o','u')

    fun String.hasDoubleLetter() = doubleLetterRegex.containsMatchIn(this)

    fun String.has3OrMoreVowels() = count { it in vowels } >= 3

    fun String.has2PairsOfLetters() = twoPairsRegex.containsMatchIn(this)

    fun String.hasSandwitchLetter() = sandwitchRegex.containsMatchIn(this)

    fun String.isNicePart1(): Boolean {
        return has3OrMoreVowels() && hasDoubleLetter() && !exludedPairsRegex.containsMatchIn(this)
    }

    fun String.isNicePart2(): Boolean {
        return hasSandwitchLetter() && has2PairsOfLetters()
    }

    val lines = File("input.txt").readLines()

    println(lines.count { it.isNicePart1() } )

    println(lines.count { it.isNicePart2() } )
}
