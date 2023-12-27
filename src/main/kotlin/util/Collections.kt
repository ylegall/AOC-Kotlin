package util

import java.util.ArrayDeque
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

inline fun <T> Collection<T>.split(delimiter: (T) -> Boolean): List<List<T>> {
    val chunks = mutableListOf<List<T>>()
    val currentChunk = mutableListOf<T>()
    for (item in this) {
       if (delimiter(item)) {
           chunks.add(currentChunk.toList())
           currentChunk.clear()
       } else {
           currentChunk.add(item)
       }
    }
    if (currentChunk.isNotEmpty()) {
        chunks.add(currentChunk.toList())
    }
    return chunks
}

inline fun <reified T : Enum<T>> enumSetOf(vararg items: T): EnumSet<T> =
    EnumSet.noneOf(T::class.java).apply {
    addAll(items)
}

fun <A, B> Pair<A, B>.swap(): Pair<B, A> {
    return second to first
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

fun <T> arrayDequeOf(vararg items: T) = ArrayDeque<T>(items.size).apply {
    addAll(items)
}

fun <T> List<T>.replace(index: Int, newValue: T): List<T> = toMutableList().apply {
    set(index, newValue)
}

fun Iterable<Long>.product(initialValue: Long = 1L): Long {
    var sum = initialValue
    for (element in this) {
        sum *= element
    }
    return sum
}

fun Iterable<Int>.product(initialValue: Int = 1): Int {
    var sum = initialValue
    for (element in this) {
        sum *= element
    }
    return sum
}

fun <T> MutableList<MutableList<T>>.transpose() {
    for (row in this.indices) {
        for (col in row + 1 until this[row].size) {
            val tmp = this[row][col]
            this[row][col] = this[col][row]
            this[col][row] = tmp
        }
    }
}

fun List<String>.rotate(): List<String> {
    val newRows = mutableListOf<String>()
    (0 until this.first().length).forEach { col ->
        val c = indices.reversed().map { row -> this[row][col] }
        newRows.add(c.joinToString(""))
    }
    return newRows
}

fun MutableList<MutableList<Char>>.rotateInPlace() {
    val n = this.size
    val maxRow = n / 2

    for (row in 0 until maxRow) {
        for (col in row until n - row - 1) {
            var currentRow = row
            var currentCol = col
            val temp = this[row][col]
            // inner cycle
            for (i in 0 until 3) {
                val newRow = n - 1 - currentCol
                val newCol = currentRow
                this[currentRow][currentCol] = this[newRow][newCol]
                currentRow = newRow
                currentCol = newCol
            }
            this[currentRow][currentCol] = temp
        }
    }
}