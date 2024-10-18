package aoc2019


fun main() {

    val low = 206938
    val high = 679128

    fun Int.digitList(): List<Int> {
        var number = this
        val digits = MutableList(6) { 0 }
        for (i in 5 downTo 0) {
            digits[i] = number % 10
            number /= 10
        }
        return digits
    }

    fun Int.isIncreasing() = digitList().zipWithNext().all { (first, second) ->
        first <= second
    }

    fun Int.hasRepeatOfAtLeast2() = digitList().zipWithNext().any { (first, second) ->
        first == second
    }

    fun countValidPasswordsPart1() = (low .. high).filter {
        it.isIncreasing() && it.hasRepeatOfAtLeast2()
    }.size

    fun countValidPasswordsPart2() = (low .. high).filter {
        it.isIncreasing()
    }.filter {
        val digitCounts = it.digitList().groupingBy { it }.eachCount()
        digitCounts.any { it.value == 2 }
    }.size

    println(countValidPasswordsPart1())
    println(countValidPasswordsPart2())
}
