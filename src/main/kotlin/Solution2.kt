import java.util.stream.Stream

fun checksum(stream: Stream<String>): Int {
    var count2 = 0
    var count3 = 0
    val counts = hashMapOf<Char, Int>()
    for (line in stream) {
        line.trim().forEach { c ->
            counts[c] = counts.getOrDefault(c, 0) + 1
        }
        if (counts.values.contains(2)) count2++
        if (counts.values.contains(3)) count3++
        counts.clear()
    }
    return count2 * count3
}

fun findMatch(stream: Stream<String>): String {
    val patterns = hashSetOf<String>()
    for (line in stream) {
        val options = line.mapIndexed { i, _ ->
            line.substring(0, i) + line.substring(i+1)
        }
        options.forEach { if (it in patterns) return it }
        options.forEach { patterns.add(it) }
    }
    throw Exception("match not found")
}

fun main() {
    input("inputs/input-2.txt").use { lines ->
        println(checksum(lines))
    }
    input("inputs/input-2.txt").use { lines ->
        println(findMatch(lines))
    }
}