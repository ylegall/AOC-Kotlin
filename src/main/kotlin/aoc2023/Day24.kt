package aoc2023

import java.io.File

private object Day24 {

    data class Vector(
        val x: Double, val y: Double, val z: Double
    ) {
        operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)

        operator fun minus(other: Vector) = Vector(x - other.x, y - other.y, z - other.z)

        operator fun times(other: Double) = Vector(x * other, y * other, z * other)

        fun cross(other: Vector) = Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )

        fun dot(other: Vector) = x * other.x + y * other.y + z * other.z
    }

    data class HailStone(
        val pos: Vector,
        val vel: Vector
    ) {
        fun projectXY() = HailStone(
            Vector(pos.x, pos.y, 0.0),
            Vector(vel.x, vel.y, 0.0)
        )
    }

    fun parseInput() = File("input.txt").readLines().map { line ->
        line.split(',', '@').map { it.trim().toDouble() }.let {
            HailStone(
                Vector(it[0], it[1], it[2]),
                Vector(it[3], it[4], it[5])
            )
        }
    }

    fun intersectsXY(a: HailStone, b: HailStone): Vector? {
        val dp = b.pos - a.pos
        val det = b.vel.x * a.vel.y - b.vel.y * a.vel.x
        if (det == 0.0) return null
        val u = (dp.y * b.vel.x - dp.x * b.vel.y) / det
        val v = (dp.y * a.vel.x - dp.x * a.vel.y) / det
        return if (u >= 0 && v >= 0) {
            a.pos + a.vel * u
        } else {
            null
        }
    }

    fun part1(input: List<HailStone>) {
        var count = 0
        val low = 200000000000000.0
        val high = 400000000000000.0
        for (i in 0 until input.size-1) {
            for (j in i+1 until input.size) {
                val pos = intersectsXY(input[i].projectXY(), input[j].projectXY())
                if (pos != null && pos.x in low .. high && pos.y in low .. high) {
                    count++
                }
            }
        }
        println(count)
    }

    fun part2(input: List<HailStone>) {
        val p1 = input[1].pos - input[0].pos
        val p2 = input[2].pos - input[0].pos
        val v1 = input[1].vel - input[0].vel
        val v2 = input[2].vel - input[0].vel
        val t1 = -(p1.cross(p2).dot(v2) / v1.cross(p2).dot(v2))
        val t2 = -(p1.cross(p2).dot(v1) / p1.cross(v2).dot(v1))
        val c1 = input[1].pos + input[1].vel * t1
        val c2 = input[2].pos + input[2].vel * t2
        val v = (c2 - c1) * (1.0 / (t2 - t1))
        val p = c1 - (v * t1)
        println(p.x.toLong() + p.y.toLong() + p.z.toLong())
    }

}

fun main() {
    val input = Day24.parseInput()
    Day24.part1(input)
    Day24.part2(input)
}