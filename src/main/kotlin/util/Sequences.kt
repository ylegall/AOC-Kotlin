package util

fun <T> Sequence<T>.repeat() = sequence {
    while (true) {
        yieldAll(this@repeat)
    }
}

fun <T> Sequence<T>.repeat(times: Long) = sequence {
    for (i in 0L until times) {
        yieldAll(this@repeat)
    }
}

fun <T> Sequence<T>.repeat(times: Int) = repeat(times.toLong())