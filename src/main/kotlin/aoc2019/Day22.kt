package aoc2019

import java.io.File
import java.math.BigInteger


fun main() {

    fun modExp(x: Long, e: Long, m: Long): Long {
        var x = x.toBigInteger()
        val m = m.toBigInteger()
        var result = BigInteger.ONE
        var n = e
        while (n > 0) {
            if (n and 1 == 1L) {
                result = (result * x) % m
            }
            n /= 2
            x = (x * x) % m
        }
        return result.toLong()
    }

    // only works when m is prime
    fun modInverse(a: Long, m: Long): Long {
        return modExp(a, m - 2, m)
    }

    class LinearFunction(
        val a: Long,
        val b: Long
    ) {
        operator fun invoke(x: Long, m: Long) = // (a * x + b) % m
            (a.toBigInteger() * x.toBigInteger() + b.toBigInteger()).mod(m.toBigInteger()).toLong()

        fun compose(other: LinearFunction, m: Long) = LinearFunction(
            (a.toBigInteger() * other.a.toBigInteger()).mod(m.toBigInteger()).toLong(),
            (other.a.toBigInteger() * b.toBigInteger() + other.b.toBigInteger()).mod(m.toBigInteger()).toLong()
        )

        fun exp(e: Long, m: Long): LinearFunction {
            val newA = modExp(a, e, m)
            val num = b.toBigInteger() * (newA - 1).toBigInteger()
            val den = modInverse(a - 1, m).toBigInteger()
            val newB = (num * den).mod(m.toBigInteger()).toLong()
            return LinearFunction(newA, newB)
        }

        fun inverse(m: Long): LinearFunction {
            val newA = modInverse(a, m)
            val newB = (newA.toBigInteger() * (-b).toBigInteger()).mod(m.toBigInteger()).toLong()
            return LinearFunction(newA, newB)
        }

    }

    fun deal() = LinearFunction(-1L, -1L)

    fun cut(n: Long) = LinearFunction(1L, -n)

    fun increment(n: Long) = LinearFunction(n, 0L)

    fun parseInput(): List<LinearFunction> {
        return File("input.txt").useLines { lines ->
            lines.map { line ->
                if (line == "deal into new stack") {
                    deal()
                } else {
                    val tokens = line.split(" ")
                    if (tokens[0] == "cut") {
                        cut(tokens.last().toLong())
                    } else {
                        increment(tokens.last().toLong())
                    }
                }
            }.toList()
        }
    }

    fun part1() {
        val deckSize = 10007L
        val function = parseInput()
            .reduce { f1, f2 -> f1.compose(f2, deckSize) }
        println(function(2019, deckSize))
    }

    fun part2() {
        val deckSize = 119315717514047L
        val times = 101741582076661L
        val composed = parseInput().reduce { f1, f2 -> f1.compose(f2, deckSize) }
        val repeated = composed.exp(times, deckSize)
        val result = repeated.inverse(deckSize)(2020, deckSize)
        println(result % deckSize)
    }

    part1()
    part2()
}
