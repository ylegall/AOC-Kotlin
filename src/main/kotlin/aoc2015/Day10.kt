package aoc2015

import java.lang.StringBuilder

private const val INPUT = "1113222113"

private fun lookSay(seq: String): String {
    val buff = StringBuilder()
    var count = 1
    var char = seq[0]
    for (i in 1 until seq.length) {
        if (seq[i] == char) {
            count++
        } else {
            buff.append(count).append(char)
            count = 1
            char = seq[i]
        }
    }
    return buff.append(count).append(char).toString()
}

private fun lookSaySequenceLength(times: Int): Int {
    var seq = INPUT
    repeat(times) {
        seq = lookSay(seq)
    }
    return seq.length
}

fun main() {
    println(lookSaySequenceLength(40))
    println(lookSaySequenceLength(50))
}