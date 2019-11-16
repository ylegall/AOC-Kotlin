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

inline fun <reified T : Enum<T>> enumSetOf(vararg items: T): EnumSet<T> =
    EnumSet.noneOf(T::class.java).apply {
    addAll(items)
}