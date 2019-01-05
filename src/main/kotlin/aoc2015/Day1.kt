package aoc2015

import util.input
import kotlin.streams.toList

private fun countParens(parens: String): Int {
    return parens.map { if (it == '(') 1 else -1 }.sum()
}

private tailrec fun firstNegativeIndex(index: Int, sum: Int, parens: String): Int {
    if (index > parens.length) return -1
    if (sum == -1) return index
    val newSum = sum + if (parens[index] == '(') 1 else -1
    return firstNegativeIndex(index + 1, newSum, parens)
}

fun main() {
    // part 1
    val parens = input("inputs/2015/1.txt").use {
        it.toList().joinToString("").filter { it != '(' || it != ')' }
    }
    println(countParens(parens))

    // part 2
    println(firstNegativeIndex(0, 0, parens))
}