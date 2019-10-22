package aoc2017

import java.util.*

private const val STEPS = 316

private fun findNumberAfterInsertions(insertions: Int): Int {
    var counter = 2
    var index = 1
    val list = LinkedList<Int>()
    list.add(0)
    list.add(1)
    var iterator = list.listIterator()
    repeat(insertions - 1) {
        val nextIndex = (STEPS + index) % list.size
        if (nextIndex < index) {
            iterator = list.listIterator(nextIndex + 1)
        } else {
            repeat(nextIndex - index) {
                iterator.next()
            }
        }
        iterator.add(counter++)
        index = nextIndex + 1
    }
    return iterator.next()
}

private fun findNumberAfterZero(insertions: Int): Int {
    var numberAfterZero = 0
    var index = 0
    var size = 1
    repeat(insertions) {
        val nextIndex = (STEPS + index) % size
        if (nextIndex == 0) {
            numberAfterZero = size
        }
        index = nextIndex + 1
        size++
    }
    return numberAfterZero
}

fun main() {
    println(findNumberAfterInsertions(2017))
    println(findNumberAfterZero(50000000))
}