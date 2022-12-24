package aoc2022

import java.io.File

fun main() {

    data class Node(val value: Long, val rank: Int)

    val input = File("input.txt").readLines()
        .mapIndexed { index, s -> Node(s.toLong(), index) }

    fun nextIndex(current: Int, delta: Long, size: Int): Int {
        return (current + delta).mod(size - 1)
    }

    fun List<Node>.move(fromIndex: Int, toIndex: Int): List<Node> {
        val item = get(fromIndex)
        return if (toIndex > fromIndex) {
            val prefix = take(fromIndex)
            val mid = slice(fromIndex + 1 .. toIndex)
            val suffix = slice(toIndex + 1 until size)
            prefix + mid + listOf(item) + suffix
        } else {
            val prefix = take(toIndex)
            val mid = slice(toIndex until fromIndex)
            val suffix = slice(fromIndex + 1 until size)
            prefix + listOf(item) + mid +  suffix
        }
    }

    fun mix(list: List<Node>): List<Node> {
        var result = list
        for (i in list.indices) {
            val currentIndex = result.indexOfFirst { it.rank == i }
            val (value, _) = result[currentIndex]
            val targetIndex = nextIndex(currentIndex, value, input.size)
            result = result.move(currentIndex, targetIndex)
            // println("$i: ${result.map { it.value }}")
        }
        return result
    }

    fun part1() {

        val result = mix(input)
        val zeroIndex = result.indexOfFirst { it.value == 0L }
        val coordinate = listOf(1000, 2000, 3000).sumOf { idx ->
            val index = (idx + zeroIndex) % input.size
            result[index].value
        }
        println(coordinate)
    }

    fun part2() {
        var result = input.map { Node(it.value * 811589153L, it.rank) }
        repeat(10) { result = mix(result) }
        val zeroIndex = result.indexOfFirst { it.value == 0L }
        val coordinate = listOf(1000, 2000, 3000).sumOf { idx ->
            val index = (idx + zeroIndex) % input.size
            result[index].value
        }
        println(coordinate)
    }

    part1()
    part2()
}