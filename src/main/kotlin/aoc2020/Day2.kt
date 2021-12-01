package aoc2020

import java.io.File


private object Day2 {

    private val pattern = Regex("""(\d+)-(\d+) (\w): (\w+)""")

    data class PasswordPolicy(
            val low: Int,
            val high: Int,
            val char: Char,
            val password: String
    )

    fun validPasswordsPart1(policies: List<PasswordPolicy>): Int {
        return policies.count { (low, high, char, password) ->
            val charCounts = password.groupingBy { it }.eachCount()
            val count = charCounts[char] ?: 0
            count in low..high
        }
    }

    fun validPasswordsPart2(policies: List<PasswordPolicy>): Int {
        return policies.count { (low, high, char, password) ->
            (password[low - 1] == char) xor (password[high - 1] == char)
        }
    }

    fun parseLines(line: String): PasswordPolicy {
        val match = pattern.find(line) ?: throw Exception("bad input line: $line")
        val (low, high, char, password) = match.destructured
        return PasswordPolicy(low.toInt(), high.toInt(), char[0], password)
    }

}

fun main() {
    val policies = File("inputs/2020/2.txt").useLines { lines ->
        lines.map { Day2.parseLines(it) }.toList()
    }
    println(Day2.validPasswordsPart1(policies))
    println(Day2.validPasswordsPart2(policies))
}
