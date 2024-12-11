package aoc2024

import java.io.File

fun main() {

    data class Equation(val target: Long, val terms: List<Long>)


    val input = File("input.txt").readLines().map { line ->
        val parts = line.split(": ")
        val target = parts[0].toLong()
        val terms = parts[1].split(" ").map { it.toLong() }
        Equation(target, terms)
    }

    fun concat(a: Long, b: Long): Long {
        return "$a$b".toLong()
    }

    fun findOperators2(equation: Equation, part2: Boolean = false): List<Char> {
        fun recurse(value: Long, index: Int, operators: List<Char>): List<Char> {
            return when {
                value > equation.target -> emptyList()
                index == equation.terms.size -> {
                    if (value == equation.target) operators else emptyList()
                }
                else -> {
                    val nextTerm = equation.terms[index]
                    listOf(
                        recurse(value * nextTerm, index + 1, operators + '*'),
                        if (part2) recurse(concat(value, nextTerm), index + 1, operators + '|') else emptyList(),
                        recurse(value + nextTerm, index + 1, operators + '+')
                    ).find { it.isNotEmpty() } ?: emptyList()
                }
            }
        }
        return recurse(equation.terms.first(), 1, emptyList())
    }

    fun part1() {
        val result = input.sumOf { equation ->
            if (findOperators2(equation).isNotEmpty()) equation.target else 0
        }
        println(result)
    }

    fun part2() {
        val result = input.sumOf { equation ->
            if (findOperators2(equation, true).isNotEmpty()) equation.target else 0
        }
        println(result)
    }

    part1()
    part2()
}