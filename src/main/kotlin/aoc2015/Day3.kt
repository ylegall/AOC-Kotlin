package aoc2015

import util.Point
import util.input
import kotlin.streams.toList

private fun countVisitedHouses(dirs: String): Set<Point> {
    val visited = hashSetOf(Point(0, 0))
    dirs.map { dir ->
        when (dir) {
            '<' -> Point(-1, 0)
            '>' -> Point(1, 0)
            '^' -> Point(0, -1)
            'v' -> Point(0, 1)
            else -> throw Exception("unknown input: $dir")
        }
    }.reduce { current, next ->
        visited.add(current)
        Point(current.x + next.x, current.y + next.y)
    }.let {
        visited.add(it)
    }
    return visited
}

private fun countVisitedHousesInPairs(dirs: String): Set<Point> {
    val pairs = dirs.chunked(2)
    val santaDirs = pairs.map { it[0] }.joinToString("")
    val roboDirs = pairs.map { it[1] }.joinToString("")
    return countVisitedHouses(santaDirs) + countVisitedHouses(roboDirs)
}

fun main() {
    val dirs = input("inputs/2015/3.txt").use {
        it.toList().joinToString("")
    }

    // part 1
    println(countVisitedHouses(dirs).size)

    // part 2
    println(countVisitedHousesInPairs(dirs).size)
}