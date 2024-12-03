package aoc2024

import java.io.File

object Day3 {
    sealed class Operation {
        class Mul(val a: Long, val b: Long): Operation()
        data object Enable: Operation()
        data object Disable: Operation()
    }
}

fun main() {

    val multPattern = """mul\((\d{1,3}),(\d{1,3})\)"""
    val multRegex = multPattern.toRegex()

    fun parseInput1(): Sequence<Pair<Long, Long>> {
        val text = File("input.txt").readText()
        return multRegex.findAll(text).map { match ->
            Pair(
                match.groups[1]?.value?.toLong()!!,
                match.groups[2]?.value?.toLong()!!
            )
        }
    }

    fun part1() {
        println(parseInput1().sumOf { pair ->
            pair.first * pair.second
        })
    }

    val fullPattern = """$multPattern|don't\(\)|do\(\)"""
    val fullRegex = fullPattern.toRegex()

    fun parseInput2(): Sequence<Day3.Operation> {
        val text = File("input.txt").readText()
        return fullRegex.findAll(text).map { match ->
            val op = match.groups[0]!!.value.take(3)
            when (op) {
                "mul" -> Day3.Operation.Mul(match.groups[1]!!.value.toLong(), match.groups[2]!!.value.toLong())
                "do(" -> Day3.Operation.Enable
                "don" -> Day3.Operation.Disable
                else -> throw Exception("parsing failed")
            }
        }
    }

    fun part2() {
        var enabled = true
        var sum = 0L
        for (op in parseInput2()) {
            when (op) {
                Day3.Operation.Disable -> enabled = false
                Day3.Operation.Enable -> enabled = true
                is Day3.Operation.Mul -> if (enabled) {
                    sum += op.a * op.b
                }
            }
        }
        println(sum)
    }

    part1()
    part2()
}