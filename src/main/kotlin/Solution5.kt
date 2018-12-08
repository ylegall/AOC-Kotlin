
private const val DIFF = 'a' - 'A'

fun canCollapse(c1: Char, c2: Char) = Math.abs(c1 - c2) == DIFF

fun collapse(str: String) = str.fold("") { result, next ->
    if (result.isNotEmpty() && canCollapse(result.last(), next)) {
        result.dropLast(1)
    } else {
        result + next
    }
}

fun main() {
    val line = input("inputs/input-5.txt").use { it.findFirst().get() }

    println(collapse(line).length)

    val minLength = ('a' .. 'z')
            .map {
                line.replace(it.toString(), "", ignoreCase = true)
            }.map {
                collapse(it).length
            }.min()
    println(minLength)
}