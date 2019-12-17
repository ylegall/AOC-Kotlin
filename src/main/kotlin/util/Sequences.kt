package util

fun <T> Sequence<T>.repeat() = sequence {
    while (true) {
        yieldAll(this@repeat)
    }
}

fun <T> Sequence<T>.repeat(times: Int) = sequence {
    repeat(times) {
        yieldAll(this@repeat)
    }
}