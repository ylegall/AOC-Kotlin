package aoc2018

import java.io.File


private fun firstRepeat(list: List<Int>): Int {
    val seen = hashSetOf(0)
    var sum = 0
    while (true) {
        for (item in list) {
            sum += item
            if (sum in seen) {
                return sum
            }
            seen.add(sum)
        }
    }
}

fun main() {
    val list = File("inputs/2018/1.txt").useLines { lines ->
        lines.map { it.toInt() }.toList()
    }
    println(list.sum())
    println(firstRepeat(list))
}