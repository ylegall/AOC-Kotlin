package aoc2018

private const val SERIAL_NO = 7672
private const val LEN = 300

private fun hundredsDigit(x: Int): Int {
    return if (x < 100) {
        0
    } else {
        (x / 100) % 10
    }
}

private fun power(x: Int, y: Int): Int {
    val rackId = x + 10
    return hundredsDigit(((rackId * y) + SERIAL_NO) * rackId) - 5
}

private fun powerGridSums(): Array<IntArray> {
    val grid = Array(LEN) { IntArray(LEN) { 0 } }
    grid[0][0] = power(0, 0)
    for (i in 1 until LEN) {
        grid[0][i] = grid[0][i-1] + power(i, 0)
        grid[i][0] = grid[i-1][0] + power(0, i)
    }

    for (y in 1 until LEN) {
        for (x in 1 until LEN) {
            grid[y][x] = power(x, y) + grid[y-1][x] + grid[y][x-1] - grid[y-1][x-1]
        }
    }
    return grid
}

private fun maxPowerSum(k: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val grid = powerGridSums()

    val k1 = k - 1
    var maxX = k1
    var maxY = k1
    var maxSum = grid[maxX][maxY]

    for (i in k until LEN) {
        val rowSum = grid[k1][i] - grid[k1][i-k]
        if (rowSum > maxSum) {
            maxSum = rowSum
            maxX = i
        }
        val colSum = grid[i][k1] - grid[i-k][k1]
        if (colSum > maxSum) {
            maxSum = colSum
            maxY = i
        }
    }
    for (y in k until LEN) {
        for (x in k until LEN) {
            val sum = grid[y][x] - grid[y-k][x] - grid[y][x-k] + grid[y-k][x-k]
            if (sum > maxSum) {
                maxSum = sum
                maxX = x; maxY = y
            }
        }
    }

    return Pair(Pair(maxX - k1, maxY - k1), Pair(k, maxSum))
}

fun main() {
    println(maxPowerSum(3))
    val maxSquare = (1..300).map { maxPowerSum(it) }.maxBy { it.second.second }
    println(maxSquare)
}