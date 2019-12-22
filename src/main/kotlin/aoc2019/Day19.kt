package aoc2019

private fun countPointsInRangeOfBeam(codes: Iterable<Long>) {
    var totalPoints = 0

    for (y in 0 until 50) {
        for (x in 0 until 50) {
            if (testTractorBeam(codes, x, y) == 1) {
                totalPoints++
                print('#')
            } else {
                print('.')
            }
        }
        println()
    }

    println(totalPoints)
}

private fun testTractorBeam(codes: Iterable<Long>, x: Int, y: Int): Int {
    var output = -1L
    val inputs = sequence {
        yield(x)
        yield(y)
    }.iterator()

    val processor = intCodeProcessor(codes) {
        inputSupplier = { inputs.next().toLong() }
        outputConsumer = { output = it; pause() }
    }
    processor.run()
    return output.toInt()
}

fun main() {
    val codes = loadIntCodeInstructions("inputs/2019/19.txt")
    countPointsInRangeOfBeam(codes)
}