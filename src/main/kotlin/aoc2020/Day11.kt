package aoc2020

import java.io.File

// TODO: clean up
private object Day11 {

    private val dirs = listOf(
            -1 to 0,
            -1 to 1,
            0 to 1,
            1 to 1,
            1 to 0,
            1 to -1,
            0 to -1,
            -1 to -1,
    )

    class SeatGrid(input: List<String>) {
        val gridA = input.map { it.toCharArray() }
        val gridB = input.map { it.toCharArray() }
        var curr = gridA
        var prev = gridB

        fun swap() {
            val tmp = curr
            curr = prev
            prev = tmp
        }

        operator fun get(i: Int, j: Int): Char {
            return curr.getOrNull(i)?.getOrNull(j) ?: '.'
        }

        fun neighbors1(row: Int, col: Int): Int {
            var emptySeats = 0
            var occupiedSeats = 0
            for (i in -1 .. 1) {
                for (j in -1 .. 1) {
                    if (i == 0 && j == 0) continue
                    val cell = this[row + i, col + j]
                    when (cell) {
                        'L' -> emptySeats++
                        '#' -> occupiedSeats++
                    }
                }
            }
            return occupiedSeats
        }


        fun neighbors2(row: Int, col: Int): Int {
            var occupiedSeats = 0
            for ((i, j) in dirs) {
                var di = i
                var dj = j
                while (true) {
                    val cell = curr.getOrNull(di + row)?.getOrNull(dj + col)

                    if (cell == null || cell == 'L') {
                        break
                    } else if (cell == '#') {
                        occupiedSeats++
                        break
                    }

                    di += i
                    dj += j
                }
            }
            return occupiedSeats
        }

        fun step(algo: Int): Boolean {
            var changed = false
            for (row in curr.indices) {
                for (col in curr[row].indices) {
                    val cell = this[row, col]
                    if (cell != '.') {

                        val occupied = if (algo == 1) {
                            neighbors1(row, col)
                        } else {
                            neighbors2(row, col)
                        }

                        val limit = if (algo == 1) 4 else 5

                        if (cell == 'L' && occupied == 0) {
                            changed = true
                            prev[row][col] = '#'
                        } else if (cell == '#' && occupied >= limit) {
                            changed = true
                            prev[row][col] = 'L'
                        } else {
                            prev[row][col] = cell
                        }
                    }
                }
            }
            swap()
            return changed
        }

        fun print() {
            println(curr.joinToString("\n") { it.contentToString() })
        }
    }

    fun countSeatsAfterEquelibrium(algo: Int, input: List<String>): Int {
        val grid = SeatGrid(input)
        var steps = 0
        do {
            val changed = grid.step(algo)
            steps++
            //println(steps)
            //grid.print()
        } while (changed)
        println("stopped after $steps steps")
        return grid.curr.map { row -> row.count { seat -> seat == '#' } }.sum()
    }

}


fun main() {
    val input = File("inputs/2020/11.txt").readLines()
    println(Day11.countSeatsAfterEquelibrium(1, input))
    println(Day11.countSeatsAfterEquelibrium(2, input))
}