package aoc2023

import util.split
import java.io.File

private typealias Part = Map<Char, Int>

fun main() {

    data class Condition(
        val category: Char,
        val op: Char,
        val value: Int,
        val dest: String
    )

    data class Workflow(
        val name: String,
        val rules: List<Condition>,
        val lastDest: String
    ) {
        fun processPart(part: Part): String {
            for (rule in rules) {
                val inputValue = part[rule.category]!!
                val testResult = when (rule.op) {
                    '<' -> inputValue < rule.value
                    '>' -> inputValue > rule.value
                    else -> throw Exception("bad operator")
                }
                if (testResult) return rule.dest
            }
            return lastDest
        }
    }

    fun parsePart(line: String): Map<Char, Int> {
        val tokens = line.substring(1 until line.length-1).split(",")
        return tokens.associate { token ->
            token.first() to token.substring(2).toInt()
        }
    }

    fun parseCondition(str: String): Condition {
        val tokens = str.split(":")
        val category = tokens[0][0]
        val op = tokens[0][1]
        val value = tokens[0].substring(2).toInt()
        val dest = tokens[1]
        return Condition(category, op, value, dest)
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

    fun reduceRange(inputRange: LongRange, mid: Long, op: Char): LongRange {
        return when(op) {
            '<' -> {
                when {
                    mid >= inputRange.last -> inputRange
                    mid < inputRange.first -> LongRange.EMPTY
                    else -> inputRange.first until mid
                }
            }
            '>' -> {
                when {
                    mid > inputRange.last -> LongRange.EMPTY
                    mid <= inputRange.first -> inputRange
                    else -> (mid+1) .. inputRange.last
                }
            }
            else -> throw Exception("invalid op")
        }
    }

    data class QuadRange(
        val ranges: Map<Char, LongRange> = "xmas".associateWith { 1L..4000L }
    ) {
        fun reduceBy(category: Char, op: Char, midpoint: Long): QuadRange {
            val newRange = reduceRange(ranges[category]!!, midpoint, op)
            return QuadRange(ranges + (category to newRange))
        }

        fun totalCombinations(): Long {
            return ranges.values.fold(1L) { product, range -> product * (range.last - range.first + 1) }
        }
    }

    fun part1() {
        val (workflows, parts) = parseInput("input.txt")
        val acceptedParts = mutableSetOf<Part>()

        for (part in parts) {
            if (isPartValid(part, workflows)) {
                acceptedParts.add(part)
            }
        }

        println(acceptedParts.sumOf { it.values.sum() })
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
                        val (category, op, mid, dest) = rule
                        val newQuadRange = remainingQuadRange.reduceBy(category, op, mid.toLong())
                        recurse(dest, newQuadRange)

                        val (negativeOp, negativeMid) = if (op == '>') {
                            '<' to (mid + 1L)
                        } else {
                            '>' to (mid - 1L)
                        }
                        remainingQuadRange = remainingQuadRange.reduceBy(category, negativeOp, negativeMid)
                    }
                    recurse(workflow.lastDest, remainingQuadRange)
                }
            }
        }
        recurse("in", QuadRange())
        println(totalAccepted)
    }

    part1()
    part2()
}