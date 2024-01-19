package util



fun ByteArray.toHex(): String = joinToString("") { byte -> "%02x".format(byte) }

fun String.sorted() = this.toCharArray().sorted().joinToString("")

inline fun Iterable<String>.findPoints(crossinline predicate: (Char) -> Boolean): Sequence<Point> = asSequence().mapIndexed { y, row ->
    row.mapIndexed { x, item ->
        Point(x, y) to item
    }
}.flatten().filter {
    predicate(it.second)
}.map {
    it.first
}