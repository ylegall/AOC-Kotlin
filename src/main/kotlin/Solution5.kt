
private const val DIFF = 97 - 65

fun canCollapse(c1: Char, c2: Char): Boolean {
    return Math.abs(c1 - c2) == DIFF
}

fun collapse(str: String): String {
    return str.fold("") { result, next ->
        if (result.isNotEmpty() && canCollapse(result.last(), next)) {
            result.dropLast(1)
        } else {
            result + next
        }
    }
}

fun main() {
    input("inputs/input-5.txt").use { lines ->
        for (line in lines) {
            println(collapse(line).length)
        }
    }
    input("inputs/input-5.txt").use { lines ->
        for (line in lines) {
            val minLength = ('a' .. 'z')
                    .map {
                        line.replace(it.toString(), "", ignoreCase = true)
                    }.map {
                        collapse(it).length
                    }.min()
            println(minLength)
        }
    }
}