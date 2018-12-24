package aoc2018

import kotlin.system.measureTimeMillis

const val INPUT = 681901

private val next = ArrayList<Byte>(2)

private fun nextRecipes(a: Byte, b: Byte): List<Byte> {
    next.clear()
    val result = (a + b).toByte()
    if (result < 10) {
        next.add(result)
    } else {
        next.add(1)
        next.add((result % 10).toByte())
    }
    return next
}

private fun lastTenRecipesAfter(index: Int): String {
    val recipes = arrayListOf<Byte>(3, 7)
    var a = 0
    var b = 1
    while (recipes.size < index + 10) {
        recipes.addAll(nextRecipes(recipes[a], recipes[b]))
        a = (a + 1 + recipes[a]) % recipes.size
        b = (b + 1 + recipes[b]) % recipes.size
    }
    return recipes.takeLast(10).joinToString("")
}

private fun numRecipesBefore(targetSequence: List<Byte>): Int {
    val recipes = arrayListOf<Byte>(3, 7)
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
    val elapsed = measureTimeMillis {
        println(lastTenRecipesAfter(INPUT))
        println(numRecipesBefore(INPUT.toString().map { Character.getNumericValue(it).toByte() }))
    }
    println(elapsed)
}