import kotlin.streams.toList

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
    val list = input("inputs/input-1.txt").use { lines ->
        lines.map { it.toInt() }.toList()
    }
    println(list.sum())
    println(firstRepeat(list))
}