package util

fun <T: Comparable<T>> max(vararg items: T) = items.maxOrNull()

fun <T: Comparable<T>> min(vararg items: T) = items.minOrNull()

fun gcd(x: Int, y: Int) = gcd(x.toLong(), y.toLong()).toInt()

tailrec fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) {
        x
    } else {
        gcd(y, x % y)
    }
}

fun gcd(vararg items: Long) = items.reduce { a, b -> gcd(a, b) }

fun lcm(x: Int, y: Int) = (x * y) / gcd(x, y)

fun lcm(x: Long, y: Long) = (x * y) / gcd(x, y)

fun lcm(vararg items: Long) = items.reduce { a, b -> lcm(a, b) }

fun lcm(vararg items: Int) = items.reduce { a, b -> lcm(a, b) }

fun sign(x: Int) = if (x < 0) -1 else 1

infix fun Int.ceilDiv(divisor: Int): Int = -Math.floorDiv(-this, divisor)

infix fun Long.ceilDiv(divisor: Long): Long = -Math.floorDiv(-this, divisor)