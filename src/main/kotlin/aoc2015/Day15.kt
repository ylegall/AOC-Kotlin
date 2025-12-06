package aoc2015

import java.io.File


private object Day15 {

    private const val LIMIT = 100

    data class Ingredient(
        val name: String,
        val properties: List<Int>
    )

    data class Cookie(
        val score: Int,
        val calories: Int
    )

    fun parseInput(): List<Ingredient> {
        return File("input.txt").useLines { lines ->
            lines.map { line ->
                line.split(',', ' ', ':')
                    .filter {
                        it.isNotEmpty()
                    }.let {
                        val props = it.drop(1).chunked(2) { (_, value) -> value.toInt() }
                        Ingredient(it.first(), props)
                    }
            }.toList()
        }
    }

    fun makeCookie(ingredients: List<Ingredient>, portions: List<Int>): Cookie {
        return ingredients.zip(portions).map { (ingredient, amount) ->
            ingredient.properties.map { it * amount }
        }.reduce { props1, props2 ->
            props1.zip(props2).map { it.first + it.second }
        }.map {
            it.coerceAtLeast(0)
        }.let {
            Cookie(
                it.dropLast(1).reduce(Int::times),
                it.last()
            )
        }
    }

    fun generateCookies(
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
}

fun main() {
    val ingredients = Day15.parseInput()
    println(Day15.generateCookies(ingredients).maxOf { it.score })
    println(Day15.generateCookies(ingredients).filter { it.calories == 500 }.maxOf { it.score })
}
