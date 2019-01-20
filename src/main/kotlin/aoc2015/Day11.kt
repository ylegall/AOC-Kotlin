package aoc2015

private const val INPUT = "hxbxwxba"

private val doubleLetterPattern = Regex("""([a-z])\1""")
private val restrictedLetters = Regex("[ilo]")

private fun String.hasIncreasingTriplet() = (0 .. length - 3).find {
    this[it] == this[it + 1] - 1 && this[it] == this[it + 2] - 2
} != null

private fun String.isSecure() = !this.contains(restrictedLetters) &&
    doubleLetterPattern.findAll(this).count() >= 2 &&
    hasIncreasingTriplet()


private fun String.inc(): String {
    val chars = toCharArray()
    var digit = length - 1
    while (digit >= 0) {
        var c = chars[digit].inc()
        if (c == 'i' || c == 'l' || c == 'o') {
            c = c.inc()
        }
        if (c > 'z') {
            chars[digit] = 'a'
            digit--
        } else {
            chars[digit] = c
            break
        }
    }
    return String(chars)
}

fun main() {
    val firstPw = generateSequence(INPUT.inc()) { it.inc() }.find { it.isSecure() }!!
    println(firstPw)

    val secondPw = generateSequence(firstPw.inc()) { it.inc() }.find { it.isSecure() }
    println(secondPw)
}