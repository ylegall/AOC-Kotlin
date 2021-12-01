package aoc2015

import kotlin.math.min

private fun findPartitions(
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

private fun Iterable<Int>.product() = fold(1L) { a, b -> a * b }

private fun findSmallestGroupProduct(groups: Set<Set<Int>>): Long {
    return groups.groupBy {
        it.size
    }.minByOrNull {
        it.key
    }?.value?.minByOrNull {
        it.product()
    }?.product() ?: 0L
}

fun main() {
    val items = listOf(1, 2, 3, 7, 11, 13, 17, 19, 23, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113)
    val sum = items.sum()
    println(findSmallestGroupProduct(findPartitions(sum / 3, items)))
    println(findSmallestGroupProduct(findPartitions(sum / 4, items)))
}