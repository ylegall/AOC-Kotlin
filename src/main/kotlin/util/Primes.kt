package util

import java.util.BitSet

fun primesUntil(limit: Int): Sequence<Int> = sequence {
    val composites = BitSet(limit)
    var i = 2
    while (i <= limit) {
        if (!composites[i]) {
            yield(i)
        }
        for (j in i..limit step i) {
            composites[j] = true
        }
        i += 1
    }
}

fun primesBetween(start: Int, end: Int): Sequence<Int> = primesUntil(end).dropWhile { it < start }