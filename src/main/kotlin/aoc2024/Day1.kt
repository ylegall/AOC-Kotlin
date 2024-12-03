import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("input.txt").readLines().map { line ->
        line.split(Regex("""\s+""")).map { it.toInt() }
    }

    fun part1() {
        val first = input.map { it[0] }.sorted()
        val second = input.map { it[1] }.sorted()
        val totalDistance = first.zip(second).sumOf { abs(it.first - it.second)  }
        println(totalDistance)
    }

    fun part2() {
        val rightCount = input.map { it[1] }.groupingBy { it }.eachCount()
        val score = input.asSequence().map { it[0] }
            .sumOf { it * (rightCount[it] ?: 0) }
        println(score)
    }

    part1()
    part2()
}