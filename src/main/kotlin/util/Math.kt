package util

fun <T: Comparable<T>> max(vararg items: T) = items.max()

fun <T: Comparable<T>> min(vararg items: T) = items.min()

fun gcd(x: Int, y: Int) = gcd(x.toLong(), y.toLong()).toInt()

tailrec fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) {
        x
    } else {
        gcd(y, x % y)
    }
}
