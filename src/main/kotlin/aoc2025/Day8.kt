package aoc2025

import util.UnionFind
import util.product
import java.io.File
import kotlin.math.pow

private object Day8 {

    data class Coord(
        val x: Int,
        val y: Int,
        val z: Int
    )

    fun parseInput(): List<Coord> {
        return File("input.txt").readLines()
            .map { line -> line.split(",").let {
                    Coord(it[0].toInt(), it[1].toInt(), it[2].toInt())
                }
            }
    }

    fun distanceSquared(coord1: Coord, coord2: Coord): Double {
        return (
            (coord2.x - coord1.x).toDouble().pow(2.0) +
            (coord2.y - coord1.y).toDouble().pow(2.0) +
            (coord2.z - coord1.z).toDouble().pow(2.0)
        )
    }

    fun sortedPairs(input: List<Coord>) =
        (0 until input.size-1)
            .flatMap { i ->
                (i+1 until input.size).map { j -> i to j }
            }.sortedBy {
                distanceSquared(input[it.first], input[it.second])
            }

    fun part1And2(input: List<Coord>) {
        val uf = UnionFind(input.size)
        val pairs = sortedPairs(input)

        // part 1
        val iterator = pairs.iterator()
        repeat(1000) {
            val (i, j) = iterator.next()
            if (!uf.connected(i, j)) {
                uf.union(i, j)
            }
        }
        val result = uf.groupSizes().values
            .sortedDescending()
            .take(3)
            .product()
        println(result)

        // part 2
        while (iterator.hasNext()) {
            val (i, j) = iterator.next()
            uf.union(i, j)
            if (uf.size == 1) {
                println(input[i].x.toLong() * input[j].x.toLong())
                break
            }
        }
    }
}

fun main() {
    val input = Day8.parseInput()
    Day8.part1And2(input)
}