package util


class MutableCounter<T>(
        private val counts: MutableMap<T, Long> = mutableMapOf()
): MutableMap<T, Long> by counts {

    fun increment(key: T, units: Long = 1) {
        counts.compute(key) { _, oldCount ->
            val newCount = if (oldCount == null) units else oldCount + units
            if (newCount == 0L) null else newCount
        }
    }

    fun decrement(key: T, units: Long = 1) = increment(key, -units)

    override operator fun get(key: T): Long = counts.getOrDefault(key, 0L)

    operator fun set(key: T, value: Long) {
        counts[key] = value
    }

    operator fun plusAssign(key: T) = increment(key, 1L)

    fun copy() = MutableCounter(counts.toMutableMap())

    override fun toString() = counts.toString()
}

fun <T> mutableCounterOf(counts: Map<T, Int>) = MutableCounter(
    counts.mapValues { it.value.toLong() }.toMutableMap()
)

fun <T> counts(items: Iterable<T>) = mutableCounterOf(items.groupingBy { it }.eachCount())