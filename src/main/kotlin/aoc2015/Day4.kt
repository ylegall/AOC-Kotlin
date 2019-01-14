package aoc2015

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


private const val INPUT = "iwrupvqb"

private fun firstHashWithPrefix(key: String, target: String): Int {
    val md = MessageDigest.getInstance("MD5")
    return generateSequence(1) { it + 1 }.first {
        val input = "$key$it".toByteArray()
        DatatypeConverter.printHexBinary(md.digest(input)).startsWith(target)
    }
}

fun main() {
    // part 1:
    println(firstHashWithPrefix(INPUT, "00000"))

    // part 2:
    println(firstHashWithPrefix(INPUT, "000000"))
}