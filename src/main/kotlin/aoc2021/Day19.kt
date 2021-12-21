package aoc2021

import java.io.File
import kotlin.math.max

fun main() {

    data class Vec(val x: Int, val y: Int, val z: Int) {

        operator fun plus(v: Vec) = Vec(x + v.x, y + v.y, z + v.z)

        operator fun minus(v: Vec) = Vec(x - v.x, y - v.y, z - v.z)

        override fun toString() = "($x, $y, $z)"

        fun rotate(axis: Vec) = when (axis) {
            Vec(1, 0, 0)  -> Vec(x, -z, y)
            Vec(-1, 0, 0) -> Vec(x, z, -y)
            Vec(0, 1, 0)  -> Vec(z, y, -x)
            Vec(0, -1, 0) -> Vec(-z, y, x)
            Vec(0, 0, 1)  -> Vec(-y, x, z)
            Vec(0, 0, -1) -> Vec(y, -x, z)
            else -> throw Exception("bad axis")
        }

    }

    fun List<Vec>.rotate(axis: Vec) = map { it.rotate(axis) }

    fun orientations(beacons: List<Vec>): List<List<Vec>> {
        var orientations = mutableListOf<List<Vec>>()
        var axis = Vec(1, 0, 0)
        var beacons = beacons
        repeat(4) {
            repeat(4) {
                orientations.add(beacons)
                beacons = beacons.rotate(axis)
            }
            axis = axis.rotate(Vec(0, 1, 0))
            beacons = beacons.rotate(Vec(0, 1, 0))
        }

        axis = axis.rotate(Vec(0, 0, 1))
        beacons = beacons.rotate(Vec(0, 0, 1))
        repeat(4) {
            orientations.add(beacons)
            beacons = beacons.rotate(axis)
        }

        repeat(2) {
            axis = axis.rotate(Vec(0, 0, 1))
            beacons = beacons.rotate(Vec(0, 0, 1))
        }
        repeat(4) {
            orientations.add(beacons)
            beacons = beacons.rotate(axis)
        }
        return orientations
    }

    fun findOverlappingBeacons(beacons1: Set<Vec>, beacons2: List<Vec>): List<Vec> {
        for (v1 in beacons1) {
            for (v2 in beacons2) {
                val delta = v1 - v2
                val aligned = beacons2.map { it + delta }
                val alignedSet = aligned.toSet()
                val count = beacons1.count { it in alignedSet }
                if (count >= 12) {
                    return aligned
                }
            }
        }
        return emptyList()
    }

    fun part1(input: List<List<Vec>>): Int {

        val reference = input[0]
        val scannerSet = mutableSetOf(0)
        val allBeacons = reference.toMutableSet()

        var i = 1
        while (scannerSet.size < input.size) {
            if (i !in scannerSet) {
                for (orientation in orientations(input[i])) {
                    val matches = findOverlappingBeacons(allBeacons, orientation)
                    if (matches.isNotEmpty()) {
                        scannerSet.add(i)
                        allBeacons.addAll(matches)
                        break
                    }
                }
            }
            i = (i + 1) % input.size
        }
        //println(allBeacons.sortedWith(compareBy({ it.x }, { it.y }, { it.z })).joinToString("\n"))
        return allBeacons.size
    }

    val reports = File("inputs/2021/input.txt").useLines { lines ->
        val reports = mutableListOf<List<Vec>>()
        var beacons = mutableListOf<Vec>()
        for (line in lines) {
            when {
                line.startsWith("---") -> continue
                line.isEmpty() -> {
                    reports.add(beacons)
                    beacons = mutableListOf()
                }
                else -> {
                    val tokens = line.split(",").map { it.toInt() }
                    beacons.add(Vec(tokens[0], tokens[1], tokens[2]))
                }
            }
        }
        if (beacons.isNotEmpty()) {
            reports.add(beacons)
        }
        reports
    }

    println(part1(reports))
//    println(part2())

//    println(
//        orientations(listOf(Vec(1, 0, 0), Vec(0, 0, 1))).joinToString("\n")
//    )
}