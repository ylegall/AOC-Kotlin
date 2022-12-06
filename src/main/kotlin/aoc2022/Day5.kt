package aoc2022

import java.io.File

fun main() {

    data class Move(
        val count: Int,
        val from: Int,
        val to: Int
    )

    class Input(
        val stacks: Array<ArrayDeque<Char>>,
        val moves: List<Move>
    )

    fun parseInput(): Input {
        val stacks = Array(9) {  ArrayDeque<Char>() }
        val moves = mutableListOf<Move>()

        val lines = File("input.txt").readLines()

        val initialLines = lines.takeWhile { it.isNotEmpty() }.dropLast(1)
        for (line in initialLines) {
            for (i in stacks.indices) {
                line.getOrNull(4 * i + 1)?.takeIf { it.isLetter() }?.let {
                    stacks[i].addFirst(it)
                }
            }
        }

        val remainingLines = lines.drop(initialLines.size + 2)
        for (line in remainingLines) {
            val tokens = line.split(" ")
            moves.add(Move(tokens[1].toInt(), tokens[3].toInt() - 1, tokens[5].toInt() - 1))
        }

        return Input(stacks, moves)
    }


    fun part1() {
        val input = parseInput()
        for (move in input.moves) {
            repeat(move.count) {
                input.stacks[move.to].addLast(input.stacks[move.from].removeLast())
            }
        }
        println(input.stacks.map { it.lastOrNull() }.joinToString("") { it.toString() })
    }

    fun part2() {
        val input = parseInput()
        val stacks = input.stacks
        for (move in input.moves) {
            val temp = mutableListOf<Char>()
            repeat(move.count) {
                temp.add(stacks[move.from].removeLast())
            }
            for (item in temp.reversed()) {
                stacks[move.to].addLast(item)
            }
        }
        println(stacks.map { it.lastOrNull() }.joinToString("") { it.toString() })
    }

    part1()
    part2()
}
