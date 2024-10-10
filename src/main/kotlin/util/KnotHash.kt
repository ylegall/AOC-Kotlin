package util


fun knotHashToString(
        lengths: Iterable<Int>,
        suffix: List<Int> = listOf(17, 31, 73, 47, 23),
        iterations: Int = 64
): String {
    return knotHash(lengths, suffix, iterations).toHex()
}

fun knotHash(
        input: Iterable<Int>,
        suffix: List<Int> = listOf(17, 31, 73, 47, 23),
        iterations: Int = 64,
        condense: Boolean = true
): List<Int> {
    var index = 0
    var skip = 0
    val data = CircularArray(Array(256) { it })
    val lengths = input + suffix
    repeat(iterations) {
        for (length in lengths) {
            data.reverseLength(index, length)
            index = (index + length + skip) //% data.size
            skip++
        }
    }
    return if (condense) {
        data.toList().chunked(16).map { block ->
            block.reduce { a, b ->
                a xor b
            }
        }
    } else {
        data.toList()
    }
}

fun Iterable<Int>.toHex() = joinToString("") { "%02x".format(it) }