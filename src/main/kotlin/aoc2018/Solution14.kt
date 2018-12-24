package aoc2018

const val INPUT = 681901

private fun nextRecipes(a: Int, b: Int): List<Int> {
    return (a + b).toString().map { Character.getNumericValue(it) }
}

private fun lastTenRecipesAfter(index: Int): String {
    val recipes = arrayListOf(3, 7)
    var a = 0
    var b = 1
    while (recipes.size < index + 10) {
        recipes.addAll(nextRecipes(recipes[a], recipes[b]))
        a = (a + 1 + recipes[a]) % recipes.size
        b = (b + 1 + recipes[b]) % recipes.size
    }
    return recipes.takeLast(10).joinToString("")
}

private fun numRecipesBefore(targetSequence: List<Int>): Int {
    val recipes = arrayListOf(3, 7)
    var a = 0
    var b = 1
    while (true) {
        val nextDigits = nextRecipes(recipes[a], recipes[b])
        nextDigits.forEach {
            recipes.add(it)
            if (recipes.takeLast(6) == targetSequence) {
                return recipes.size - targetSequence.size
            }
        }
        a = (a + 1 + recipes[a]) % recipes.size
        b = (b + 1 + recipes[b]) % recipes.size
    }
}

fun main() {
    println(lastTenRecipesAfter(INPUT))
    println(numRecipesBefore(INPUT.toString().map { Character.getNumericValue(it) }))
}