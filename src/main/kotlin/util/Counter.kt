package util

class Counter<K>(
    private val counts: Map<K, Int> = emptyMap(),
    private val defaultValue: Int = 0
): Map<K, Int> by counts {

    override operator fun get(key: K): Int = counts.getOrDefault(key, defaultValue)

    fun increment(key: K, amount: Int = 1): Counter<K> {
        val newKeyCount = get(key) + amount
        return Counter(counts + (key to newKeyCount))
    }

    operator fun plus(other: Counter<K>): Counter<K> {
        val newKeys = counts.keys.union(other.counts.keys)
        return Counter(newKeys.associateWith { key -> this[key] + other[key] }, defaultValue)
    }

    operator fun minus(other: Counter<K>): Counter<K> {
        return plus(other * -1)
    }

    operator fun times(factor: Int): Counter<K> {
        return Counter(counts.mapValues { it.value * factor })
    }

    override fun toString() = counts.toString()
}

fun <K> counterOf(vararg pairs: Pair<K, Int>, defaultValue: Int = 0): Counter<K> {
    return Counter(mapOf(*pairs), defaultValue)
}

fun <K> Map<K, Int>.toCounter() = Counter(this)