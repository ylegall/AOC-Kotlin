package aoc2019

private val codes = loadIntCodeInstructions("inputs/2019/19.txt")

private fun testTractorBeam(x: Int, y: Int): Int {
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

private fun countPointsInRangeOfBeam() {
    var totalPoints = 0
    for (y in 0 until 50) {
        for (x in 0 until 50) {
            if (testTractorBeam(x, y) == 1) {
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

private fun corners(x: Int, y: Int) = listOf(
        x to y,
        x to y - 99,
        x + 99 to y - 99,
        x + 99 to y
)

private fun findCornerOfFirstSquareOf100() {
    var row = 50
    var col = 1
    do {
        col = generateSequence(col) { it + 1 }.first { testTractorBeam(it, row) != 0 }
        val corners = corners(col, row)
        if (corners.drop(1).all { testTractorBeam(it.first, it.second) != 0 }) {
            println(corners[1])
            val (x, y) = corners[1]
            col = x
            row = y
            break
        }
        row++
    } while (true)
    println(col * 10000 + row)
}

fun main() {
    countPointsInRangeOfBeam()
    findCornerOfFirstSquareOf100()
}