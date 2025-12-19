package aoc2015

import java.io.File
import java.security.MessageDigest
import kotlin.experimental.and
import kotlin.streams.asStream

private object Day4 {

    fun parseInput() = File("input.txt").readText()

    fun firstHashWithPrefixParallel(key: String): Int {
        val halfMask = 0xf0.toByte()
        val zb = 0.toByte()
        return generateSequence(1) { it + 1 }.asStream().parallel().filter {
            val input = (key + it.toString()).toByteArray()
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(input)
            bytes[0] == zb && bytes[1] == zb && (bytes[2] and halfMask) == zb
        }.findFirst().get()
    }

    fun firstHashWithPrefixParallel2(key: String): Int {
        val zb = 0.toByte()
        return generateSequence(1) { it + 1 }.asStream().parallel().filter {
            val input = (key + it.toString()).toByteArray()
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(input)
            bytes[0] == zb && bytes[1] == zb && bytes[2] == zb
        }.findFirst().get()
    }

}

fun main() {
    val input = Day4.parseInput()

    // part 1:
    println(Day4.firstHashWithPrefixParallel(input))

    // part 2:
    println(Day4.firstHashWithPrefixParallel2(input))
}