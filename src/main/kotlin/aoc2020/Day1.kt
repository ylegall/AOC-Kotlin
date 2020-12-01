package aoc2020

import java.io.File
import java.lang.IllegalStateException

private const val TARGET_SUM = 2020

fun productOfPair(numbers: List<Int>): Int {
    val seen = HashSet<Int>()
    for (number in numbers) {
        val difference = TARGET_SUM - number
        if (difference in seen) {
            return number * difference
        }
        seen.add(number)
    }
    throw IllegalStateException("no pairs found")
}

fun productOfTriple(numbers: List<Int>): Int {
    val pairs = numbers.flatMap { a ->
        numbers.map { b ->
            a + b to (a to b)
        }
    }.toMap()

    for (number in numbers) {
        val difference = TARGET_SUM - number
        pairs[difference]?.let { (a, b) ->
            return a * b * number
        }
    }

    throw IllegalStateException("no triple found")
}

fun main() {
    val numbers = File("inputs/2020/1.txt").readLines().map { it.toInt() }

    println(productOfPair(numbers))

    println(productOfTriple(numbers))
}