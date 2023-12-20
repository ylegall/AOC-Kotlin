package aoc2023

import util.split
import java.io.File

private typealias Part = Map<Char, Int>

fun main() {

    data class Rule(
        val category: Char,
        val lessThan: Boolean,
        val value: Int,
        val destination: String
    )

    data class Workflow(
        val name: String,
        val rules: List<Rule>,
        val defaultDestination: String
    ) {
        fun processPart(part: Part): String {
            for (rule in rules) {
                val inputValue = part[rule.category]!!
                val testResult = if (rule.lessThan) {
                    inputValue < rule.value
                } else {
                    inputValue > rule.value
                }
                if (testResult) return rule.destination
            }
            return defaultDestination
        }
    }

    fun parsePart(line: String): Map<Char, Int> {
        val tokens = line.substring(1 until line.length-1).split(",")
        return tokens.associate { token ->
            token.first() to token.substring(2).toInt()
        }
    }

    fun parseCondition(str: String): Rule {
        val tokens = str.split(":")
        val category = tokens[0][0]
        val lessThan = tokens[0][1] == '<'
        val value = tokens[0].substring(2).toInt()
        val dest = tokens[1]
        return Rule(category, lessThan, value, dest)
    }

    fun parseWorkflow(line: String): Workflow {
        val tokens = line.split("{", ",")
        val name = tokens[0]
        val lastDest = tokens.last().dropLast(1)
        val rules = tokens.slice(1 until tokens.size-1).map { parseCondition(it) }
        return Workflow(name, rules, lastDest)
    }

    fun parseInput(filename: String): Pair<Map<String, Workflow>, List<Part>> {
        val input = File(filename).readLines().split { it.isBlank() }
        val workflows = input[0].map { parseWorkflow(it) }.associateBy { it.name }
        val parts = input[1].map { parsePart(it) }
        return workflows to parts
    }

    fun isPartValid(part: Part, workflows: Map<String, Workflow>): Boolean {
        var currentFlow = workflows["in"]!!
        while (true) {
            when (val nextFlow = currentFlow.processPart(part)) {
                "A" -> return true
                "R" -> return false
                else -> {
                    currentFlow = workflows[nextFlow]!!
                }
            }
        }
    }

    fun reduceRange(inputRange: LongRange, mid: Long, lessThan: Boolean): LongRange {
        return if (lessThan) {
            when {
                mid >= inputRange.last -> inputRange
                mid < inputRange.first -> LongRange.EMPTY
                else -> inputRange.first until mid
            }
        } else {
            when {
                mid > inputRange.last -> LongRange.EMPTY
                mid <= inputRange.first -> inputRange
                else -> (mid+1) .. inputRange.last
            }
        }
    }

    data class QuadRange(
        val ranges: Map<Char, LongRange> = "xmas".associateWith { 1L..4000L }
    ) {
        fun reduceBy(category: Char, lessThan: Boolean, midpoint: Long): QuadRange {
            val newRange = reduceRange(ranges[category]!!, midpoint, lessThan)
            return QuadRange(ranges + (category to newRange))
        }

        fun totalCombinations(): Long {
            return ranges.values.fold(1L) { product, range -> product * (range.last - range.first + 1) }
        }
    }

    fun part1() {
        val (workflows, parts) = parseInput("input.txt")
        val result = parts.sumOf { part ->
            if (isPartValid(part, workflows)) {
                part.values.sum()
            } else {
                0
            }
        }
        println(result)
    }

    fun part2() {
        val (workflows, _) = parseInput("input.txt")
        var totalAccepted = 0L

        fun recurse(flow: String, quadRange: QuadRange) {
            when (flow) {
                "R" -> { }
                "A" -> {
                    totalAccepted += quadRange.totalCombinations()
                }
                else -> {
                    val workflow = workflows[flow]!!
                    var remainingQuadRange = quadRange
                    for (rule in workflow.rules) {
                        val (category, lessThan, mid, dest) = rule
                        val newQuadRange = remainingQuadRange.reduceBy(category, lessThan, mid.toLong())
                        recurse(dest, newQuadRange)

                        val negativeMid = if (lessThan) mid - 1L else mid + 1L
                        remainingQuadRange = remainingQuadRange.reduceBy(category, !lessThan, negativeMid)
                    }
                    recurse(workflow.defaultDestination, remainingQuadRange)
                }
            }
        }
        recurse("in", QuadRange())
        println(totalAccepted)
    }

    part1()
    part2()
}