package aoc2018

import util.CircularList
import java.io.File


private fun firstRepeat(list: List<Int>): Int {
    val seen = hashSetOf(0)
    var sum = 0
    for (item in CircularList(list)) {
        sum += item
        if (sum in seen) {
            break
        }
        seen.add(sum)
    }
    return sum
}

fun main() {
    val list = File("inputs/2018/1.txt").useLines { lines ->
        lines.map { it.toInt() }.toList()
    }
    println(list.sum())
    println(firstRepeat(list))
}