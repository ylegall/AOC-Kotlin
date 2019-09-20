package aoc2016

import kotlin.system.measureTimeMillis


private object Day16 {

    const val PART1_SIZE = 272
    const val PART2_SIZE = 35651584
    private const val INITIAL_SEED = "11110010111001001"

    private fun generateData(seed: String, amount: Int): String {
        var result = seed
        while (result.length < amount) {
            result = transformString(result)
        }
        return result.substring(0 until amount)
    }

    private fun transformString(str: String): String {
        val buffer = StringBuilder(str).append('0')
        for (i in str.length - 1 downTo 0) {
            buffer.append(if (str[i] == '0') '1' else '0')
        }
        return buffer.toString()
    }

    private fun generateChecksum(str: String): String {
        var input = str
        val buffer = StringBuilder()
        do {
            buffer.clear()
            for (i in input.indices step 2) {
                if (input[i] == input[i+1]) {
                    buffer.append("1")
                } else {
                    buffer.append("0")
                }
            }
            input = buffer.toString()
        } while (input.length % 2 == 0)
        return input
    }

    fun run(size: Int) {
        val elapsed = measureTimeMillis {
            val data = generateData(INITIAL_SEED, size)
            val checksum = generateChecksum(data)
            println(checksum)
        }
        println("elapsed: $elapsed ms")
    }
}

fun main() {
    Day16.run(Day16.PART1_SIZE)
    Day16.run(Day16.PART2_SIZE)
}