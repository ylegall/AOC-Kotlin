package aoc2015

private const val INPUT = 3400000

private fun Int.factors(): Set<Int> {
    val limit = Math.sqrt(this.toDouble()).toInt()
    val factors = hashSetOf(1, this)
    for (i in 2 .. limit) {
        if (this % i == 0) {
            factors.add(i)
            factors.add(this/i)
        }
    }
    return factors
}

private fun Int.factors2(): Set<Int> {
    val limit = Math.sqrt(this.toDouble()).toInt()
    val factors = hashSetOf(1, this)
    for (i in 2 .. limit) {
        if (this % i == 0) {
            val div = this / i
            if (div <= 50) factors.add(i)
            if (i <= 50) factors.add(div)
        }
    }
    return factors
}

private fun houseGifts1() = generateSequence(0) {
    it + 1
}.mapIndexed { idx, houseNumber ->
    idx to houseNumber.factors().sum()
}.first {
    it.second >= INPUT
}

private fun houseGifts2() = generateSequence(0) {
    it + 1
}.mapIndexed { idx, houseNumber ->
    idx to houseNumber.factors2().sum() * 11
}.first {
    it.second >= INPUT * 10
}

// 786240
fun main() {
    println(houseGifts1())
    println(houseGifts2())
}