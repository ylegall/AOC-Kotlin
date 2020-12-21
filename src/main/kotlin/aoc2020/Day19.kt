package aoc2020

import java.io.File


private object Day19 {

    private val terminals = setOf("a", "b")

    data class Rule(val id: String, val expansions: List<List<String>>)

    class SearchState(
            val buffer: List<String>,
            val prefix: String = ""
    )

    fun generateAllStrings(start: String, rules: List<Rule>): Set<String> {
        val ruleMap = rules.associateBy { it.id }
        val results = mutableSetOf<String>()

        fun generate(state: SearchState) {
            if (state.buffer.isEmpty()) {
                results.add(state.prefix)
                return
            }

            val next = state.buffer.first()
            if (next in terminals) {
                generate(SearchState(state.buffer.drop(1), state.prefix + next))
            } else {
                val options = ruleMap[next]!!.expansions
                for (symbols in options) {
                    generate(SearchState(symbols + state.buffer.drop(1), state.prefix))
                }
            }
        }

        generate(SearchState(listOf(start)))

        return results
    }

    fun matches(
            message: String,
            set42: Set<String>,
            set31: Set<String>,
    ): Boolean {
        var string = message
        var count42 = 0
        var count31 = 0

        while (string.length >= 8) {
            val prefix = string.take(8)
            if (prefix in set42) {
                string = string.drop(8)
                count42++
            } else {
                break
            }
        }

        while (string.length >= 8) {
            val prefix = string.take(8)
            if (prefix in set31) {
                string = string.drop(8)
                count31++
            } else {
                break
            }
        }

        if (string.isNotEmpty()) return false

        return count31 in 1 until count42
    }

    private fun parseRule(line: String): Rule {
        val parts = line.split(": ")
        val id = parts[0]
        val body = parts[1].split(" | ").map { it.split(" ").map { it.replace("\"", "") } }
        return Rule(id, body)
    }

    fun parseInput(input: List<String>): Pair<List<Rule>, List<String>> {
        val rules = input.takeWhile { it.isNotEmpty() }.map { parseRule(it) }
        val messages = input.dropWhile { it.isNotEmpty() }.drop(1)
        return rules to messages
    }
}

fun main() {
    val input = File("inputs/2020/19.txt").readLines()
    val (rules, messages) = Day19.parseInput(input)
    val allStrings = Day19.generateAllStrings("0", rules)
    println(messages.count { it in allStrings })

    val setA = Day19.generateAllStrings("42", rules)
    val setB = Day19.generateAllStrings("31", rules)
    println(messages.count { Day19.matches(it, setA, setB) })
}