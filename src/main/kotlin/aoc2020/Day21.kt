package aoc2020

import java.io.File


private object Day21 {

    data class Food(
            val ingredients: Set<String>,
            val allergens: Set<String>,
    )

    class Input(
            val ingredients: Set<String>,
            val foods: List<Food>
    )

    fun parseFood(line: String): Food {
        val parts = line.split(" (contains ", ")")
        val ingredients = parts[0].split(" ").toSet()
        val allergens = parts[1].split(", ").filter { it.isNotEmpty() }.toSet()
        return Food(ingredients, allergens)
    }

    fun parseInput(lines: List<String>): Input {
        val allIngredients = mutableSetOf<String>()
        //val foods = mutableListOf<Food>()
        val foods = lines.map { line ->
            parseFood(line).also { allIngredients.addAll(it.ingredients); }
        }
        return Input(allIngredients, foods)
    }

    fun findAssignments(input: Input): Map<String, String> {
        val allergenToIngredientMap = mutableMapOf<String, MutableSet<String>>()
        for (food in input.foods) {
            for (allergen in food.allergens) {
                val possibleIngredients = allergenToIngredientMap.getOrPut(allergen) { mutableSetOf() }
                if (possibleIngredients.isEmpty()) {
                    possibleIngredients.addAll(food.ingredients)
                } else {
                    possibleIngredients.retainAll(food.ingredients)
                }
            }
        }

        val assignments = mutableMapOf<String, String>()

        while (true) {
            val remaining = allergenToIngredientMap
                    .mapValues { it.value.filter { it !in assignments } }
                    .filter { it.value.isNotEmpty() }

            val singlePossibilities = remaining.asSequence()
                    .filter { it.key !in assignments }
                    .filter { it.value.size == 1 }
                    .map { it.value.first() to it.key }

            if (remaining.isEmpty()) break

            assignments.putAll(singlePossibilities)
        }
        return assignments
    }
}

fun main() {
    val input = File("inputs/2020/21.txt").readLines().let { Day21.parseInput(it) }

    val assignments = Day21.findAssignments(input)
    val safeIngredients = input.ingredients.filter { it !in assignments }.toSet()
    val safeAppearances = input.foods.map { food -> food.ingredients.intersect(safeIngredients).size }.sum()
    println(safeAppearances)

    println(assignments.entries.sortedBy { it.value }.joinToString(",") { it.key })
}