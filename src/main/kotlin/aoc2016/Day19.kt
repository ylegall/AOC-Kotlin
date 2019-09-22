package aoc2016

import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


private object Day19 {
    private const val NUM_ELVES = 3018458

    fun findLastElfOne(): Int {
        val elves = buildElves(NUM_ELVES)

        var even = false
        while (elves.size > 1) {
            val iterator = elves.listIterator()
            while (iterator.hasNext()) {
                iterator.next()
                if (even) {
                    iterator.remove()
                }
                even = !even
            }
        }
        return elves.peek()!!
    }

    fun findLastElfTwo(): Int {
        val elves = buildElves(NUM_ELVES)
        var iterator = elves.listIterator(NUM_ELVES / 2)
        while (elves.size > 1) {
            if (!iterator.hasNext()) iterator = elves.listIterator()
            iterator.next()
            iterator.remove()
            if (elves.size % 2 == 0) {
                if (!iterator.hasNext()) iterator = elves.listIterator()
                iterator.next()
            }
        }
        return elves.peek()!!
    }

    private fun buildElves(size: Int): LinkedList<Int> {
        val elves = LinkedList<Int>()
        for (i in 1 .. size) {
            elves.add(i)
        }
        return elves
    }
}

@ExperimentalTime
fun main() {
    println(measureTimedValue { Day19.findLastElfOne() })
    println(measureTimedValue { Day19.findLastElfTwo() })
}