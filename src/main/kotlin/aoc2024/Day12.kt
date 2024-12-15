package aoc2024

import util.*
import util.Direction.*
import java.io.File
import java.util.*

fun main() {


    val input = File("input.txt").readLines().map { it.toList() }

    fun label(p: Point): Char? {
        return input.getOrNull(p.y)?.getOrNull(p.x)
    }

    fun findBorders(p: Point): EnumSet<Direction> {
        val borders = enumSetOf<Direction>()
        for (dir in Direction.entries) {
            val neighbor = p.move(dir)
            if (neighbor !in input || label(neighbor) != label(p)) {
                borders.add(dir)
            }
        }
        return borders
    }

    fun findGardenGroups() {
        var id = 0
        val idMap = mutableMapOf<Point, Int>()
        val uf = UnionFind(input.size * input.first().size)

        val borderMap = mutableMapOf<Point, EnumSet<Direction>>()
        val sideMap = MutableCounter<Point>()

        for (r in input.indices) {
            for (c in input[r].indices) {
                val pos = Point(c, r)
                val borders = findBorders(pos)
                borderMap[pos] = borders

                idMap[pos] = id

                val top = pos.move(NORTH)
                val left = pos.move(WEST)

                if (top in input && label(top) == label(pos)) {
                    uf.union(id, idMap[top]!!)

                    listOf(EAST, WEST).filter {
                        it in borders && it !in borderMap[top]!!
                    }.forEach { _ ->
                        sideMap.increment(pos)
                    }
                } else {
                    if (EAST in borders) sideMap.increment(pos)
                    if (WEST in borders) sideMap.increment(pos)
                }

                if (left in input && label(left) == label(pos)) {
                    uf.union(id, idMap[left]!!)

                    listOf(NORTH, SOUTH).filter {
                        it in borders && it !in borderMap[left]!!
                    }.forEach { _ ->
                        sideMap.increment(pos)
                    }
                } else {
                    if (NORTH in borders) sideMap.increment(pos)
                    if (SOUTH in borders) sideMap.increment(pos)
                }

                id++
            }
        }

        val groups = idMap.keys.groupBy { uf.find(idMap[it]!!) }

        // part 1
        println(groups.values.sumOf {
            it.size * it.sumOf { borderMap[it]!!.size }
        })

        // part 2
        println(groups.values.sumOf {
            it.size * it.sumOf { sideMap[it] }
        })
    }

    findGardenGroups()

}