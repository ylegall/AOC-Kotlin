package aoc2015

private fun triangleNumber(x: Long) = (x * (x + 1)) / 2L

private fun sequenceNumber(row: Long, col: Long) = triangleNumber(col + row - 1L) - (row - 1L)

private fun printMatrix(rows: Int, col: Int, maxDiagonal: Int = 7) {
    for (r in 1 .. rows) {
        for (c in 1 .. col) {
            if (r + c <= maxDiagonal) {
                print(sequenceNumber(r.toLong(), c.toLong()))
                print('\t')
            }
        }
        println()
    }
}

private fun nextCode(currentCode: Long) = (currentCode * 252533L) % 33554393L

private fun findCode(): Long {
    var code = 20151125L
    val targetNum = sequenceNumber(2978, 3083).also { println(it) }
    repeat((targetNum - 1).toInt()) {
        code = nextCode(code)
    }
    return code
}

fun main() {
    println(findCode())
}