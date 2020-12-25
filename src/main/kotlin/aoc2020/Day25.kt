package aoc2020

private const val MOD = 20201227L
private const val CARD_PK = 5290733L
private const val DOOR_PK = 15231938L


private fun encryptionKey(subject: Long): Long {
    val cardLoopSize = findLoopSize(subject, CARD_PK)
    return transform(DOOR_PK, cardLoopSize)
}

private fun transform(subject: Long, loopSize: Int): Long {
    return generateSequence(1L) { it.next(subject) }.drop(loopSize).first()
}

private fun findLoopSize(subject: Long, target: Long): Int {
    return generateSequence(1L) { it.next(subject) }.indexOf(target)
}

private fun Long.next(subject: Long) = (this * subject) % MOD

fun main() {
    println(encryptionKey(7))
}