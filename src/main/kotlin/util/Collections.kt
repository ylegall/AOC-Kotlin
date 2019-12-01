package util

import java.util.EnumSet

fun <T> Iterable<T>.permutations(): List<List<T>> {
    return permutations(listOf(), this.toSet())
}

private fun <T> permutations(current: List<T>, options: Set<T>): List<List<T>> {
    return if (options.isEmpty()) {
        listOf(current)
    } else {
        options.flatMap { item ->
            permutations(current + item, options - item)
        }
    }
}

inline fun <T> Collection<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (item in this) {
        sum += selector(item)
    }
    return sum
}

inline fun <reified T : Enum<T>> enumSetOf(vararg items: T): EnumSet<T> =
    EnumSet.noneOf(T::class.java).apply {
    addAll(items)
}

fun <T> MutableList<T>.swap(a: Int, b: Int) {
    val temp = this[a]
    this[a] = this[b]
    this[b] = temp
}

fun <T> List<T>.swap(a: Int, b: Int): List<T> {
    val copy = ArrayList<T>(size)
    for (i in 0 until a) {
        copy.add(this[i])
    }
    copy.add(this[b])
    for (i in a + 1 until b) {
        copy.add(this[i])
    }
    copy.add(this[a])
    for (i in b + 1 until size) {
        copy.add(this[i])
    }
    return copy
}
