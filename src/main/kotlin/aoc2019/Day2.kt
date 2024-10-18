package aoc2019

fun main() {

    val codes = loadIntCodeInstructions("input.txt")

    fun processWithInitialSettings(codes: Iterable<Long>, a: Long = 0L, b: Long = 0L): Long {
        val processor = IntCodeProcessor(codes).apply {
            setMemoryValues(1 to a, 2 to b)
        }
        processor.run()
        return processor.read(0)
    }

    fun findValueAtPositionZero(): Long {
        return processWithInitialSettings(codes, 12, 2)
    }

    fun findInputsForTargetValue(target: Long): Long {
        for (i in 0L .. 99L) {
            for (j in 0L .. 99L) {
                val result = processWithInitialSettings(codes.toMutableList(), i, j)
                if (result == target) {
                    return 100 * i + j
                }
            }
        }
        throw Exception("target not found")
    }

    println(findValueAtPositionZero())
    println(findInputsForTargetValue(19690720L))
}