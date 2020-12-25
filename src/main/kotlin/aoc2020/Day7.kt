package aoc2020

import util.arrayDequeOf
import java.io.File

private object Day7 {

    private val rulePattern = Regex("""(\d+) (\w+ \w+) bags?""")
    private const val SHINY_GOLD = "shiny gold"

    private data class BagCount(
            val color: String,
            val count: Int
    )

    private data class BagRule(
            val color: String,
            val contents: List<BagCount>
    )

    fun parseRule(line: String): BagRule {
        val tokens = line.split(" bags contain ")
        val color = tokens[0]
        val contents = if (tokens[1].endsWith("no other bags.")) {
            emptyList()
        } else {
            rulePattern.findAll(tokens[1]).map { match ->
                BagCount(match.groupValues[2], match.groupValues[1].toInt())
            }.toList()
        }
        return BagRule(color, contents)
    }

    fun numberOfBagsContainingShineyGold(rules: List<BagRule>): Int {
        val reverseMap = rules.flatMap { rule -> rule.contents.map { it.color to rule.color } }
                .groupBy { it.first }
                .mapValues { it.value.map { it.second } }

        val containerColors = mutableListOf<String>()
        val seen = mutableSetOf<String>()
        val queue = arrayDequeOf(SHINY_GOLD)
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next !in seen) {
                seen.add(next)
                containerColors.add(next)
                val containers = reverseMap[next] ?: emptyList()
                queue.addAll(containers.filter { it !in seen })
            }
        }
        return containerColors.size - 1
    }

    fun numberOfBagsInside(rules: List<BagRule>): Long {
        val map = rules.associate { it.color to it.contents }

        var totalBags = 0L
        val q = arrayDequeOf(SHINY_GOLD to 1L)
        while (q.isNotEmpty()) {
            val (color, number) = q.poll()
            totalBags += number
            val contents = map[color]?.map { it.color to number * it.count } ?: emptyList()
            q.addAll(contents)
        }
        return totalBags - 1
    }
}

fun main() {
    val lines = File("inputs/2020/7.txt").readLines()
    val rules = lines.map { Day7.parseRule(it) }
    println(Day7.numberOfBagsContainingShineyGold(rules))
    println(Day7.numberOfBagsInside(rules))
}