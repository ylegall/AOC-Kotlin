package aoc2019


private const val LOW = 206938
private const val HIGH = 679128

private fun Int.digitList(): List<Int> {
    var number = this
    val digits = MutableList(6) { 0 }
    for (i in 5 downTo 0) {
        digits[i] = number % 10
        number /= 10
    }
    return digits
}

private fun Int.isIncreasing() = digitList().windowed(2).all { (first, second) ->
    first <= second
}

private fun Int.hasRepeatOfAtLeast2() = digitList().windowed(2).any { (first, second) ->
    first == second
}

private fun Int.hasRepeatOfExactly2(): Boolean {
    val digits = digitList()
    for (i in 1 until digits.size) {
        val a = digits.getOrNull(i - 2)
        val b = digits.getOrNull(i - 1)
        val c = digits.getOrNull(i)
        val d = digits.getOrNull(i + 1)
        if (b == c) {
            if (a == null || a != c) {
                if (d == null || d != c) {
                    return true
                }
            }
        }
    }
    return false
}

private fun countValidPasswordsPart1() = (LOW .. HIGH).filter {
    it.isIncreasing() && it.hasRepeatOfAtLeast2()
}.size

private fun countValidPasswordsPart2() = (LOW .. HIGH).filter {
    it.isIncreasing() && it.hasRepeatOfExactly2()
}.size

fun main() {
    println(countValidPasswordsPart1())
    println(countValidPasswordsPart2())
}
