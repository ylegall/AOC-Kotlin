package aoc2015

import java.io.File
import kotlin.math.min

private object Day24 {

    fun findPartitions(
        targetSum: Int,
        items: List<Int>,
        currentSet: Set<Int> = emptySet(),
        minGroupSize: Int = Int.MAX_VALUE
    ): Set<Set<Int>> {
        return if (targetSum == 0) {
            setOf(currentSet)
        } else if (items.isEmpty() || targetSum < 0) {
            emptySet()
        } else {
            val nextItem = items.first()
            if (currentSet.size <= minGroupSize) {
                val nextItems = items.drop(1)
                val groups1 = findPartitions(targetSum, nextItems, currentSet, minGroupSize)
                val newMin = if (groups1.isEmpty()) minGroupSize else min(groups1.first().size, minGroupSize)
                val groups2 = findPartitions(targetSum - nextItem, nextItems, currentSet + nextItem, newMin)
                groups1 + groups2
            } else {
                emptySet()
            }
        }
    }

    fun Iterable<Int>.product() = fold(1L) { a, b -> a * b }

    fun findSmallestGroupProduct(groups: Set<Set<Int>>): Long {
        return groups.groupBy {
            it.size
        }.minByOrNull {
            it.key
        }?.value?.minByOrNull {
            it.product()
        }?.product() ?: 0L
    }
}

fun main() {
    val items = File("input.txt").readLines().map { it.toInt() }
    val sum = items.sum()
    println(Day24.findSmallestGroupProduct(Day24.findPartitions(sum / 3, items)))
    println(Day24.findSmallestGroupProduct(Day24.findPartitions(sum / 4, items)))
}