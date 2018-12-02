import java.io.FileInputStream
import kotlin.streams.toList

private fun frequencies(filename: String): Iterable<Int> {
    FileInputStream(filename).bufferedReader().use { stream ->
        return stream.lines().map { it.trim().toInt() }.toList()
    }
}

fun sumFrequences(list: Iterable<Int>) = list.sum()

fun firstRepeat(list: Iterable<Int>): Int {
    var sum = 0
    val seen = hashSetOf(0)
    while (true) {
        for (item in list) {
            sum += item
            if (sum in seen) {
                return sum
            }
            seen.add(sum)
        }
    }
}

fun main() {
    val list = frequencies("inputs/input-1.txt")
    println(sumFrequences(list))
    println(firstRepeat(list))
}