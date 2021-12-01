package aoc2015

import util.input
import kotlin.streams.asSequence

private const val LIMIT = 100

private data class Ingredient(
        val name: String,
        val properties: List<Int>
)

private data class Cookie(
        val score: Int,
        val calories: Int
)

private fun makeCookie(ingredients: List<Ingredient>, portions: List<Int>): Cookie {
    return ingredients.zip(portions).map { (ingredient, amount) ->
        ingredient.properties.map { it * amount }
    }.reduce { props1, props2 ->
        props1.zip(props2).map { it.first + it.second }
    }.map {
        Math.max(it, 0)
    }.let {
        Cookie(
            it.dropLast(1).reduce(Int::times),
            it.last()
        )
    }
}

private fun generateCookies(
        ingredients: List<Ingredient>,
        portions: List<Int> = listOf()
): Sequence<Cookie> = sequence {
    if (portions.size == 3) {
        yield(makeCookie(ingredients, portions + (LIMIT - portions.sum())))
    } else {
        val remainingIngredients = LIMIT - portions.sum()
        (0 .. remainingIngredients).map { nextAmount ->
            yieldAll(generateCookies(ingredients, portions + nextAmount))
        }
    }
}

fun main() {
    val ingredients = input("inputs/2015/15.txt").use { lines ->
        lines.asSequence().map { line ->
            line.split(',', ' ', ':').filter {
                it.isNotEmpty()
            }.let {
                val props = it.drop(1).chunked(2) { (_, value) -> value.toInt() }
                Ingredient(
                        it.first(),
                        props
                )
            }
        }.toList()
    }
    println(generateCookies(ingredients).maxByOrNull { it.score })
    println(generateCookies(ingredients).filter { it.calories == 500 }.maxByOrNull { it.score })
}
