package util


open class CircularList<T>(open val data: List<T>): Iterable<T> {

    protected fun getIdx(idx: Int) = Math.floorMod(idx, data.size)

    operator fun get(index: Int): T {
        return data[getIdx(index)]
    }

    override fun iterator() = object: Iterator<T> {
        private var idx = 0
        override fun hasNext() = true
        override fun next() = data[getIdx(idx++)]
    }
}

class MutableCircularList<T>(override val data: MutableList<T>): CircularList<T>(data) {
    operator fun set(index: Int, value: T) {
        data[getIdx(index)] = value
    }
}

fun main() {
    val list = listOf(1, 2, 3, 4, 5)
    val ring = CircularList(list)

    println((-13 .. 13).joinToString { ring[it].toString() })
}