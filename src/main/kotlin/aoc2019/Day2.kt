package aoc2019

private object Day2 {

    private const val TARGET = 19690720

    private fun processWithInitialSettings(codes: MutableList<Int>, a: Int = 0, b: Int = 0): Int {
        codes[1] = a
        codes[2] = b
        IntCodeProcessor(codes).run()
        return codes[0]
    }

    fun findValueAtPositionZero(): Int {
        val codes = loadIntCodeInstructions("inputs/2019/2.txt")
        return processWithInitialSettings(codes, 12, 2)
    }

    fun findInputsForTargetValue(): Int {
        val codes = loadIntCodeInstructions("inputs/2019/2.txt")
        for (i in 0 .. 99) {
            for (j in 0 .. 99) {
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