package util

import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.util.stream.Stream

fun input(path: String): Stream<String> {
    return FileInputStream(path).bufferedReader().lines().map { it.trim() }
}

fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
