package util

class CircularArray<T>(private val array: Array<T>)
{
    val size = array.size

    operator fun get(idx: Int) = array[idx % array.size]

    operator fun set(idx: Int, value: T) {
        array[idx % array.size] = value
    }

    fun reverseLength(startIndex: Int, length: Int) {
        if (length >= 2) {
            reverseRange(startIndex, startIndex + length)
        }
    }

    fun reverseRange(startIndex: Int, endIndex: Int) {
        var start = startIndex
        var end = (endIndex - 1)
        while (start < end) {
            val temp = get(start)
            set(start, get(end))
            set(end, temp)
            start++
            end--
        }
    }

    fun toList() = array.toList()

    fun toArray() = array.copyOf()

    override fun toString() = "[${array.joinToString(",")}]"
}

fun <T> circularArrayOf(vararg items: T) = CircularArray(items)

fun main() {
    val array = circularArrayOf(0, 1, 2, 3, 4)
    array.let {
        it.reverseLength(3, 4)
        println(it)
    }
}