package aoc2016

import util.input
import java.util.stream.Stream
import kotlin.streams.toList

private fun validTriangle(sides: List<Int>): Boolean {
    return sides[0] + sides[1] > sides[2] &&
            sides[0] + sides[2] > sides[1] &&
            sides[1] + sides[2] > sides[0]
}

private fun Stream<String>.toListOfInts() = this.filter{
    it.isNotBlank()
}.map {
    it.split(Regex("\\s+")).map { it.toInt() }
}

fun main() {
    val count1 = input("inputs/2016/3.txt").use { lines ->
        lines.toListOfInts().filter { sides ->
            validTriangle(sides)
        }.count()
    }
    println(count1)

    val count2 = input("inputs/2016/3.txt").use { lines ->
        val list = lines.toListOfInts().toList()
        (0 until list.size - 2 step 3).flatMap { row ->
            (0 until 3).map { col ->
                listOf(list[row][col], list[row + 1][col], list[row + 2][col])
            }
        }.filter { sides ->
            validTriangle(sides)
        }.count()
    }
    println(count2)
}