package aoc2017


private object Day10 {

    fun runPart2(input: String) {
        val lengths = input.map { it.toInt() } + listOf(17, 31, 73, 47, 23)
        val hasher = KnotHasher()
        val result = hasher.hash(lengths)
        println(result)
    }

    fun runPart1(input: List<Int>) {
        val hasher = KnotHasher()
        val result = hasher.singleKnotHash(input)
        println(result[0] * result[1])
    }

    private class KnotHasher {
        private var index = 0
        private var skip = 0
        private val data = MutableList(256) { it }

        fun singleKnotHash(lengths: List<Int>): List<Int> {
            for (item in lengths) {
                data.flip(index, item)
                index = (index + item + skip) % data.size
                skip++
            }
            return data
        }

        fun hash(lengths: List<Int>): String {
            repeat(64) { singleKnotHash(lengths) }
            val condensed = data.chunked(16).map { block -> block.reduce { a, b -> a xor b }}
            return condensed.toHexString()
        }

        private fun List<Int>.toHexString() = joinToString("") { "%02x".format(it) }

        private fun <T> List<T>.getMod(index: Int) = this[index % size]

        private fun <T> MutableList<T>.setMod(index: Int, value: T) = this.set(index % size, value)

        private fun <T> MutableList<T>.flip(index: Int, length: Int): List<T> {
            val sublistCopy = (index until index + length).map { this.getMod(it) }//.also { println("Sublist: $it") }
            for (i in 0 until length) {
                this.setMod(i + index, sublistCopy.getMod(length - i - 1))
            }
            return this
        }
    }
}

fun main() {
    Day10.runPart1("212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164".split(",").map { it.toInt() })
    Day10.runPart2("212,254,178,237,2,0,1,54,167,92,117,125,255,61,159,164")
}