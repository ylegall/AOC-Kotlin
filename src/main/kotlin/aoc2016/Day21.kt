package aoc2016

import util.input
import kotlin.streams.toList

private object Day21 {

    private const val INPUT1 = "abcdefgh"
    private const val INPUT2 = "fbgdceah"

    fun String.swapPosition(i: Int, j: Int): String {
        val chars = toCharArray()
        val x = chars[i]
        val y = chars[j]
        chars[i] = y
        chars[j] = x
        return String(chars)
    }

    fun String.swapLetters(a: Char, b: Char): String {
        val buffer = StringBuilder()
        for (char in this) {
            when (char) {
                a    -> buffer.append(b)
                b    -> buffer.append(a)
                else -> buffer.append(char)
            }
        }
        return buffer.toString()
    }

    fun String.rotate(rotations: Int): String {
        val buffer = StringBuilder()
        val start = length - (rotations % length)
        for (i in start until start + length) {
            buffer.append(this[i % length])
        }
        return buffer.toString()
    }

    fun String.rotateToChar(char: Char): String {
        val idx = indexOf(char)
        val rotations = idx + if (idx >= 4) 2 else 1
        return rotate(rotations)
    }

    fun String.rotateToCharReverse(char: Char): String {
        val idx = indexOf(char)
        return if (idx % 2 != 0) {
            rotate(length - idx/2 - 1)
        } else {
            when (idx) {
                2 -> rotate(2)
                4 -> rotate(1)
                6 -> rotate(0)
                0 -> rotate(7)
                else -> throw Exception("invalid index: $idx")
            }
        }
    }

    fun String.reverse(i: Int, j: Int): String {
        return substring(0 until i) + substring(i, j + 1).reversed() + substring(j + 1, length)
    }

    fun String.move(i: Int, j:Int): String {
        val removed = substring(0, i) + substring(i+1)
        return removed.substring(0, j) + this[i] + removed.substring(j)
    }

    fun String.transform(command: List<String>): String {
        return when (command[0]) {
            "rotate" -> when (command[1]) {
                "based" -> rotateToChar(command.last()[0])
                else    -> rotate(determineRotation(command[2].toInt(), command[1], this.length))
            }
            "swap" -> when (command[1]) {
                "position" -> swapPosition(command[2].toInt(), command[5].toInt())
                "letter"   -> swapLetters(command[2][0], command[5][0])
                else       -> throw IllegalArgumentException("Invalid swap")
            }
            "reverse" -> reverse(command[2].toInt(), command[4].toInt())
            "move"    -> move(command[2].toInt(), command[5].toInt())
            else      -> throw IllegalArgumentException("Invalid command")
        }
    }

    fun String.reverseTransform(command: List<String>): String {
        return when (command[0]) {
            "rotate" -> when (command[1]) {
                "based" -> rotateToCharReverse(command.last()[0])
                else    -> rotate(determineRotationReverse(command[2].toInt(), command[1], this.length))
            }
            "swap" -> when (command[1]) {
                "position" -> swapPosition(command[2].toInt(), command[5].toInt())
                "letter"   -> swapLetters(command[2][0], command[5][0])
                else       -> throw IllegalArgumentException("Invalid swap")
            }
            "reverse" -> reverse(command[2].toInt(), command[4].toInt())
            "move"    -> move(command[5].toInt(), command[2].toInt())
            else      -> throw IllegalArgumentException("Invalid command")
        }
    }

    private fun determineRotation(rotation: Int, direction: String, length: Int) = when (direction) {
        "right" -> rotation
        "left" -> length - rotation
        else -> throw IllegalArgumentException("Invalid direction")
    }

    private fun determineRotationReverse(rotation: Int, direction: String, length: Int) = when (direction) {
        "right" -> length - rotation
        "left" -> rotation
        else -> throw IllegalArgumentException("Invalid direction")
    }

    fun run() {
        val commands = input("inputs/2016/21.txt").use { lines ->
            lines.toList().map { it.split(' ') }
        }

        // part 1
        commands.fold(INPUT1) { password, command ->
            password.transform(command)
        }.also { println(it) }

        // part 2
        commands.reversed().fold(INPUT2) { password, command ->
            password.reverseTransform(command)
        }.also { println(it) }

    }
}

fun main() {
    Day21.run()
}