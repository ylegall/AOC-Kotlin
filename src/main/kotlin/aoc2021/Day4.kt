package aoc2021

import util.Counter
import java.io.File
import java.util.*

private fun winningBoard(
    board: List<List<Int>>,
    chosenNumbers: Set<Int>
): Int? {
    var unmarkedSum = 0
    val columnCounts = Counter<Int>()
    val rowCounts = Counter<Int>()
    for (row in board.indices) {
        var rowCount = 0
        for (col in board[row].indices) {
            if (board[row][col] in chosenNumbers) {
                columnCounts.increment(col)
                rowCounts.increment(row)
                rowCount += 1
            } else {
                unmarkedSum += board[row][col]
            }
        }
    }
    if (columnCounts.values.any { it.toInt() == board.size }) {
        return unmarkedSum
    }
    if (rowCounts.values.any { it.toInt() == board.size }) {
        return unmarkedSum
    }
    return null
}

fun main() {

    val scanner = Scanner(File("inputs/2021/4.txt"))
    val numbers = scanner.nextLine().split(",").map { it.toInt() }
    val boards = mutableListOf<List<List<Int>>>()
    while (scanner.hasNext()) {
        val board = List(5) { MutableList(5) { 0 } }
        for (row in 0 until 5) {
            for (col in 0 until 5) {
                board[row][col] = scanner.nextInt()
            }
        }
        boards.add(board)
    }


    fun boardScores(
        boards: List<List<List<Int>>>,
        numbers: List<Int>
    ): List<Pair<Int, Int>> {
        val scores = mutableListOf<Pair<Int, Int>>()
        val completedBoards = mutableSetOf<Int>()
        for (i in 1 until numbers.size) {
            val chosenNumbers = numbers.slice(0 until i).toSet()
            for (b in boards.indices) {
                if (b !in completedBoards) {
                    val sum = winningBoard(boards[b], chosenNumbers)
                    if (sum != null)  {
                        scores.add(sum * numbers[i - 1] to b)
                        completedBoards.add(b)
                    }
                }
            }
        }
        return scores
    }

    fun part1(
        boards: List<List<List<Int>>>,
        numbers: List<Int>
    ): Int {
        val scores = boardScores(boards, numbers)
        return scores.first().first
    }

    fun part2(
        boards: List<List<List<Int>>>,
        numbers: List<Int>
    ): Int {
        val scores = boardScores(boards, numbers)
        return scores.last().first
    }

    println(part1(boards, numbers))
    println(part2(boards, numbers))
}