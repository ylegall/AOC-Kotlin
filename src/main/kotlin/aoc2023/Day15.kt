package aoc2023

import java.io.File

fun main() {

    data class Box(
        val lenses: MutableMap<String, Int> = mutableMapOf()
    )

    fun hash(str: String) = str.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }

    fun part1() {
        File("input.txt")
            .readText()
            .split(",")
            .sumOf { hash(it) }
            .also { println(it) }
    }

    fun part2() {
        val boxes = List(256) { Box() }

        File("input.txt").readText()
            .split(",")
            .forEach { item ->
                if (item.endsWith('-')) {
                    val label = item.dropLast(1)
                    val boxIndex = hash(label)
                    boxes[boxIndex].lenses.remove(label)
                } else {
                    val label = item.dropLast(2)
                    val focalLength = (item.last()-'0')
                    val boxIndex = hash(label)
                    boxes[boxIndex].lenses[label] = focalLength
                }
            }

        val totalFocusingPower = boxes.mapIndexed { boxNumber, box ->
            box.lenses.values.mapIndexed { lenseIndex, focalPower ->
                (lenseIndex + 1) * focalPower * (1 + boxNumber)
            }.sum()
        }.sum()

        println(totalFocusingPower)
    }

    part1()
    part2()
}