package aoc2017


private object Day15 {

    private class Generator(
            initialValue: Long,
            val factor: Long,
            val isValid: (Long) -> Boolean = { true }
    ) {

        private var value = initialValue

        fun next(): Int {
            do {
                value = (value * factor) % 2147483647L
            } while (!isValid(value))
            return value.toInt()
        }
    }

    fun isMultipleOf4(n: Long) = (n shr 2) shl 2 == n

    fun isMultipleOf8(n: Long) = (n shr 3) shl 3 == n

    fun part1() = countMatchingLowerBits(
            40_000_000,
            Generator(783, 16807),
            Generator(325, 48271)
    )

    fun part2() = countMatchingLowerBits(
            5_000_000,
            Generator(783, 16807, Day15::isMultipleOf4),
            Generator(325, 48271, Day15::isMultipleOf8)
    )

    private fun countMatchingLowerBits(
            iterations: Int,
            genA: Generator,
            genB: Generator
    ): Int {
        var matches = 0
        repeat(iterations) {
            val nextA = genA.next()
            val nextB = genB.next()
            if ((nextA shl 16) == (nextB shl 16)) {
                matches++
            }
        }
        return matches
    }
}

fun main() {
    println(Day15.part1())
    println(Day15.part2())
}