package aoc2015

private const val TARGET = 150

private val buckets = listOf(
        50, 44, 11, 49, 42, 46, 18, 32, 26, 40, 21, 7, 18, 43, 10, 47, 36, 24, 22, 40
).sorted()

private fun subsetSum(): Int {
    val grid = Array(TARGET + 1) { 0 }
    grid[0] = 1

    for (bucket in buckets) {
        for (i in TARGET downTo bucket) {
            if (grid[i - bucket] != 0) {
                grid[i] += grid[i - bucket]
            }
        }
    }
    return grid[TARGET]
}

private fun subsetSum2(
        remaining: Int = TARGET,
        bucketIndex: Int = 0,
        numBuckets: Int = 0
): Map<Int, Int> {
    if (remaining == 0) {
        return mapOf(numBuckets to 1)
    }
    if (remaining < 0 || bucketIndex >= buckets.size) {
        return emptyMap()
    }
    val m1 = subsetSum2(remaining - buckets[bucketIndex], bucketIndex + 1, numBuckets + 1)
    val m2 = subsetSum2(remaining, bucketIndex + 1, numBuckets)
    return m2.entries.map { it.key to (m1[it.key] ?: 0) + it.value }.toMap() + m1.filterNot { it.key in m2 }
}

fun main() {
    println(subsetSum())
    println(subsetSum2().minByOrNull { it.key }!!.value)
}