package aoc2024

import java.io.File
import kotlin.math.log

fun main() {
    val input = File("input.txt").readText().split(" ").map { it.toLong() }

    data class State(
        val id: Long,
        val iterations: Int
    )

    fun Long.numDigits(): Long {
        return if (this < 10.0) {
            1
        } else {
            log(this.toDouble(), 10.0).toLong() + 1L
        }
    }

    fun Long.split(): Pair<Long, Long> {
        val str = this.toString()
        return Pair(
            str.substring(0 until str.length/2).toLong(),
            str.substring(str.length/2 until str.length).toLong(),
        )
    }

    fun evaluate(state: State, cache: MutableMap<State, Long>): Long {
        return if (state in cache) {
            cache[state]!!
        } else {
            val (id, iterations) = state

            val total = when {
                iterations == 0 -> {
                    1L
                }
                id == 0L -> evaluate(State(1, iterations-1), cache)
                id.numDigits() % 2 == 0L -> {
                    val (left, right) = id.split()
                    evaluate(State(left, iterations-1), cache) + evaluate(State(right, iterations-1), cache)
                }
                else -> evaluate(State(id * 2024, iterations-1), cache)
            }
            cache[state] = total
            total
        }
    }

    fun countStones() {
        val cache = mutableMapOf<State, Long>()
        val total1 = input.sumOf { evaluate(State(it, 25), cache) }
        println(total1)

        val total2 = input.sumOf { evaluate(State(it, 75), cache) }
        println(total2)
    }

    countStones()

}