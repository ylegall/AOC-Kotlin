package aoc2017

import java.io.File
import kotlin.math.abs

private object Day20 {

    private data class Vector(val x: Int, val y: Int, val z: Int) {

        operator fun plus(vector: Vector) = Vector(
                x + vector.x,
                y + vector.y,
                z + vector.z
        )

        fun magnitude() = abs(x) + abs(y) + abs(z)
    }

    private data class Particle(var pos: Vector, var vel: Vector, val acc: Vector) {

        fun update() {
            vel += acc
            pos += vel
        }
    }

    private fun parseInput(): List<Particle> {
        return File("inputs/2017/20.txt").readLines().map { line ->
            val tokens = line.split('<', '>', '=', 'p', 'v', 'a', ',').filter {
                it.isNotBlank()
            }.map {
                it.toInt()
            }
            Particle(
                    Vector(tokens[0], tokens[1], tokens[2]),
                    Vector(tokens[3], tokens[4], tokens[5]),
                    Vector(tokens[6], tokens[7], tokens[8])
            )
        }
    }

    fun findClosestToOriginOverTime(): Int {
        val particles = parseInput()
        return particles.mapIndexed { idx, particle ->
            idx to particle
        }.minByOrNull {
            it.second.acc.magnitude()
        }?.first ?: -1
    }

    fun countParticlesAfterCollisions(): Int {
        var particles = parseInput()
        var size = particles.size
        var runs = 0
        while (runs < 10000) {
            particles.forEach { it.update() }
            particles = particles.groupBy { it.pos }.filter { it.value.size == 1 }.values.map { it.first() }
            if (size == particles.size) {
                runs++
            } else {
                runs = 0
                size = particles.size
            }
        }
        return particles.size
    }
}

fun main() {
    println(Day20.findClosestToOriginOverTime())
    println(Day20.countParticlesAfterCollisions())
}