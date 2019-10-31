package util

import java.io.File
import java.io.FileInputStream
import java.util.stream.Stream

fun input(path: String): Stream<String> {
    return FileInputStream(path).bufferedReader().lines().map { it.trim() }
}

fun inputToString(path: String) = File(path).readText()