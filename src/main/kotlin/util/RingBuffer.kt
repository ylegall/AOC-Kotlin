package util

class RingBuffer<T>(
        val capacity: Int,
        private val buffer: ArrayList<T> = ArrayList(capacity)
): MutableList<T> by buffer {
    
    constructor(collection: Collection<T>): this(collection.size, ArrayList(collection))
    
    private var index = buffer.size
    
    override fun add(element: T): Boolean {
        if (buffer.size < capacity) {
            buffer.add(element)
        } else {
            buffer[index] = element
            index = (index + 1) % buffer.size
        }
        return true
    }

    override operator fun get(index: Int) = buffer[(this.index + index) % buffer.size]
    
    override fun toString() = buffer.indices.map { get(it) }.toString()
}

fun <T> ringBufferOf(vararg items: T) = RingBuffer(items.size, arrayListOf(items))