package aoc2022

import java.io.File

fun main() {

    data class Cube(val x: Int, val y: Int, val z: Int)

    val input = File("input.txt").readLines().map { line ->
        line.split(",").map { it.toInt() }.let { Cube(it[0], it[1], it[2]) }
    }

    fun Cube.neighbors() = listOf(
        Cube(x + 1, y, z),
        Cube(x - 1, y, z),
        Cube(x, y + 1, z),
        Cube(x, y - 1, z),
        Cube(x, y, z + 1),
        Cube(x, y, z - 1),
    )

    fun part1() {
        val cubes = input.toSet()
        val surfaceArea = cubes.sumOf { cube ->
            6 - cube.neighbors().count { it in cubes }
        }
        println(surfaceArea)
    }

    fun part2() {
        val minX = input.minOf { it.x } - 1
        val minY = input.minOf { it.y } - 1
        val minZ = input.minOf { it.z } - 1

        val maxX = input.maxOf { it.x } + 1
        val maxY = input.maxOf { it.y } + 1
        val maxZ = input.maxOf { it.z } + 1

        val start = Cube(minX, minY, minZ)
        val cubes = input.toSet()
        val seen = mutableSetOf<Cube>()
        val q = ArrayDeque<Cube>()
        q.add(start)

        while (q.isNotEmpty()) {
            val pos = q.removeFirst()
            if (pos in seen) continue
            seen.add(pos)

            val next = pos.neighbors()
                .filter { it !in cubes }
                .filter {
                    it.x in minX .. maxX && it.y in minY .. maxY && it.z in minZ .. maxZ
                }
            q.addAll(next)
        }

        val outsideSurfaceArea = cubes.sumOf { cube ->
            cube.neighbors().count { it in seen }
        }
        println(outsideSurfaceArea)
    }

    part1()
    part2()
}