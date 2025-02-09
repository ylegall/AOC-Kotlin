package aoc2024

import java.io.File

fun main() {

    val MOD = 16777216L

    val input = File("input.txt").readLines().map { it.toLong() }

    fun updateSecret(secret: Long): Long {
        var value = secret
        value = (secret xor (value * 64)) % MOD
        value = (value xor (value / 32)) % MOD
        value = (value xor (value * 2048)) % MOD
        return value
    }

    fun simulate(initial: Long, rounds: Long): Sequence<Long> {
        return (0 until rounds).asSequence()
            .scan(initial) { acc, _ -> updateSecret(acc) }
    }

    fun lastDigits(initial: Long, rounds: Long) =
        simulate(initial, rounds).map { it % 10 }

    fun priceWithDeltas(initial: Long, rounds: Long) =
        lastDigits(initial, rounds)
        .zipWithNext().map { it.second to it.second - it.first }
        .windowed(4)
        .map { chunk -> chunk.last().first to chunk.map { it.second } }

    // part 1
    println(input.sumOf { simulate(it, 2000).last() })

    // part 2
    // println(priceWithDeltas(123, 10).toList())
    val deltaMap = mutableMapOf<List<Long>, Long>()
    for (initialValue in input) {
        val seen = mutableSetOf<List<Long>>()
        for ((price, deltaSequence) in priceWithDeltas(initialValue, 2000)) {
            if (deltaSequence !in seen) {
                deltaMap[deltaSequence] = (deltaMap[deltaSequence] ?: 0L) + price
                seen.add(deltaSequence)
            }
        }
    }
    println(deltaMap.entries.maxBy { it.value })
}