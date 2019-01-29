package aoc2016

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

const val INPUT = "reyedfim"

private val md5 = MessageDigest.getInstance("md5")
private fun hash(n: Int) = DatatypeConverter.printHexBinary(md5.digest((INPUT + n).toByteArray()))

private val validHashes = generateSequence(0) {
    it + 1
}.map {
    hash(it)
}.filter {
    it.startsWith("00000")
}

private fun simplePassword() = validHashes.map { it[5] }.take(8).joinToString("")

private fun complexPassword(): String {
    val slots = hashMapOf<Int, Char>()
    validHashes.filter {
        Character.getNumericValue(it[5]) in (0..7)
    }.onEach {
        val idx = Character.getNumericValue(it[5])
        slots.putIfAbsent(idx, it[6])
    }.takeWhile {
        slots.size < 8
    }.toList()

    return slots.entries.sortedBy { it.key }.map { it.value }.joinToString("")
}

fun main() {
    println(simplePassword())
    println(complexPassword())
}