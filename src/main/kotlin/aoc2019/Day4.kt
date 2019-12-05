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

private fun Int.isIncreasing() = digitList().zipWithNext().all { (first, second) ->
    first <= second
}

private fun Int.hasRepeatOfAtLeast2() = digitList().zipWithNext().any { (first, second) ->
    first == second
}

private fun countValidPasswordsPart1() = (LOW .. HIGH).filter {
    it.isIncreasing() && it.hasRepeatOfAtLeast2()
}.size

private fun countValidPasswordsPart2() = (LOW .. HIGH).filter {
    it.isIncreasing()
}.filter {
    val digitCounts = it.digitList().groupingBy { it }.eachCount()
    digitCounts.any { it.value == 2 }
}.size

fun main() {
    println(countValidPasswordsPart1())
    println(countValidPasswordsPart2())
}
