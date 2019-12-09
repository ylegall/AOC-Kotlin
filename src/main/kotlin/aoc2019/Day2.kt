package aoc2019

private object Day2 {

    private const val TARGET = 19690720L

    private fun processWithInitialSettings(codes: MutableList<Long>, a: Long = 0L, b: Long = 0L): Long {
        codes[1] = a
        codes[2] = b
        IntCodeProcessor(codes).run()
        return codes[0]
    }

    fun findValueAtPositionZero(): Long {
        val codes = loadIntCodeInstructions("inputs/2019/2.txt")
        return processWithInitialSettings(codes, 12, 2)
    }

    fun findInputsForTargetValue(): Long {
        val codes = loadIntCodeInstructions("inputs/2019/2.txt")
        for (i in 0L .. 99L) {
            for (j in 0L .. 99L) {
                val result = processWithInitialSettings(codes.toMutableList(), i, j)
                if (result == TARGET) {
                    return 100 * i + j
                }
            }
        }
        throw Exception("target not found")
    }
}

fun main() {
    println(Day2.findValueAtPositionZero())
    println(Day2.findInputsForTargetValue())
}