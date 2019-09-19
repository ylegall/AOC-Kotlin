package aoc2016

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private object Day15 {

    private data class Disc(
            val slots: Int,
            val pos: Int
    ) {
        fun getPos(t: Int) = (pos + t) % slots
    }

    private fun findFirstDropTime(discs: List<Disc>): Int {
        var t = 0
        while (true) {
            val positions = discs.mapIndexed { idx, disc -> disc.getPos(t + idx + 1) }
            //println("$t: $positions")
            if (positions.all { it == 0 }) {
                break
            }
            t++
        }
        return t
    }

    @ExperimentalTime
    fun run() {
        val discs = listOf(
            Disc(17, 5),
            Disc(19, 8),
            Disc(7, 1),
            Disc(13, 7),
            Disc(5, 1),
            Disc(3, 0),
            Disc(11, 0)
        )
        println(measureTimedValue { findFirstDropTime(discs) })
    }
}

@ExperimentalTime
fun main() {
    Day15.run()
}
