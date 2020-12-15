package aoc2020

private fun findTurnNumber(startingNumbers: List<Int>, turnLimit: Int): Int {
    var turn = startingNumbers.size + 1
    val turnRecords = startingNumbers.mapIndexed { idx, number -> number to (idx + 1 to -1) }.toMap().toMutableMap()
    var lastSpoken = startingNumbers.last()
    while (turn <= turnLimit) {
        val (lastTurn, lastLastTurn) = turnRecords[lastSpoken]!!
        val next = if (lastLastTurn == -1) {
            0
        } else {
            lastTurn - lastLastTurn
        }

        val lastSpokenTurn = turnRecords[next]?.first ?: -1
        turnRecords[next] = turn to lastSpokenTurn
        lastSpoken = next
        turn++
    }
    return lastSpoken

}

fun main() {
    val input = listOf(14,1,17,0,3,20)
    println(findTurnNumber(input, 2020))
    println(findTurnNumber(input, 30000000))
}