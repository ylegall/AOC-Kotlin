
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
    input("inputs/input-5.txt").use { lines ->
        for (line in lines) {
            println(collapse(line).length)
        }
    }
    input("inputs/input-5.txt").use { lines ->
        val start = System.currentTimeMillis()
        for (line in lines) {
            val minLength = ('a' .. 'z')
                    .map {
                        line.replace(it.toString(), "", ignoreCase = true)
                    }.map {
                        collapse(it).length
                    }.min()
            println(minLength)
        }
        println("elapsed ${System.currentTimeMillis() - start}")
    }
}