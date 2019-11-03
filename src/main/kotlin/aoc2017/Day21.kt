package aoc2017

import java.io.File


private object Day21 {

    private const val initialState = ".#./..#/###"

    private fun rotate(data: List<List<Boolean>>): List<List<Boolean>> {
        val result = List(data.size) { MutableList(data.size) { false } }
        for (row in data.indices) {
            for (col in data[row].indices) {
                result[col][data.size - 1 - row] = data[row][col]
            }
        }
        return result
    }

    private fun linearize(data: List<List<Boolean>>): String {
        return data.joinToString("/") { row ->
            row.joinToString("") { if (it) "#" else "." }
        }
    }

    private fun List<List<Boolean>>.subGrid(startRow: Int, startCol: Int, newSize: Int): List<List<Boolean>> {
        return (0 until newSize).map { row ->
            (0 until newSize).map { col ->
                this[startRow + row][startCol + col]
            }
        }
    }

    private fun List<MutableList<Boolean>>.insertChunk(chunk: List<List<Boolean>>, startRow: Int, startCol: Int) {
        for (row in 0 until chunk.size) {
            for (col in 0 until chunk.size) {
                this[startRow + row][startCol + col] = chunk[row][col]
            }
        }
    }

    private fun List<List<Boolean>>.grow(rules: Map<String, String>): List<List<Boolean>> {
        val chunkSize = when {
            size % 2 == 0 -> 2
            size % 3 == 0 -> 3
            else -> throw Exception("invalid grid size: $size")
        }
        val chunks = size / chunkSize
        val newSize = chunks * (chunkSize + 1)

        val newGrid = List(newSize) { MutableList(newSize) { false } }

        for (r in 0 until chunks) {
            for (c in 0 until chunks) {
                val chunk = subGrid(r * chunkSize, c * chunkSize, chunkSize)
                val rotations = allRotations(chunk)
                val nextState = rotations.mapNotNull { rules[it] }.first()
                val newChunk = parseToGrid(nextState)
                newGrid.insertChunk(newChunk, r * (chunkSize + 1), c * (chunkSize + 1))
            }
        }
        return newGrid
    }

    private fun allRotations(data: List<List<Boolean>>): List<String> {
        val results = mutableListOf(linearize(data))
        var temp = data
        repeat(3) {
            temp = rotate(temp)
            results.add(linearize(temp))
        }
        temp = data.reversed()
        results.add(linearize(temp))
        repeat(3) {
            temp = rotate(temp)
            results.add(linearize(temp))
        }
        return results
    }

    private fun parseToGrid(rows: String) = rows.split("/").map { row -> row.map { it == '#' } }

    private fun List<List<Boolean>>.print() {
        println(joinToString("\n") { it.joinToString("") { if (it) "#" else "." } })
    }

    private fun parseInput(): Map<String, String> {
        return File("inputs/2017/21.txt").useLines {
            it.map { line ->
                val tokens = line.split(" => ")
                tokens[0].trim() to tokens[1].trim()
            }.toMap()
        }
    }

    fun run() {
        val rules = parseInput()
        var grid = parseToGrid(initialState)

        for (i in 1 .. 18) {
            grid = grid.grow(rules)
            //grid.print()
            val trueCount = grid.map { it.count { it } }.sum()
            println("$i: $trueCount")
        }

    }
}

fun main() {
    Day21.run()
}