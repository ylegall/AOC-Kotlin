package aoc2019

import util.Counter
import util.ceilDiv
import java.io.File
import java.util.ArrayDeque

private object Day14 {

    class Reagent(val units: Long, val name: String)

    class Reaction(
            val result: Reagent,
            val inputs: List<Reagent>
    )

    fun parseInput() = File("inputs/2019/14.txt").useLines { lines ->
        lines.map { line ->
            val tokens = line.split(" => ", ", ", " ")
            check(tokens.size > 2) { "too few tokens on line: $line" }
            tokens.chunked(2).map {
                Reagent(it[0].toLong(), it[1])
            }
        }.toList().associate {
            it.last().name to Reaction(it.last(), it.dropLast(1))
        }
    }

    fun requiredOre(
            startMaterial: String,
            startAmount: Long = 1L,
            reactions: Map<String, Reaction>
    ): Long {
        var requiredOre = 0L
        val excess = Counter<String>()
        val q = ArrayDeque<Pair<String, Long>>()
        q.add(startMaterial to startAmount)
        while (q.isNotEmpty()) {
            val (material, amount) = q.remove()
            if (material == "ORE") {
                requiredOre += amount
                continue
            }

            val excessAmount = excess[material]
            when {
                amount == excessAmount -> excess.remove(material)
                amount < excessAmount -> excess.decrement(material, amount)
                amount > excessAmount -> {
                    val remainingAmount = amount - excessAmount
                    excess.remove(material)
                    val reaction = reactions[material]!!
                    val multiplier = remainingAmount ceilDiv reaction.result.units
                    excess.increment(reaction.result.name, (multiplier * reaction.result.units) - remainingAmount)
                    q.addAll(reaction.inputs.map { input ->
                        input.name to input.units * multiplier
                    })
                }
            }
        }
        return requiredOre
    }

    fun maxFuelForOre(
            amount: Long,
            reactions: Map<String, Reaction>
    ): Long {
        var low = 1L
        var high = amount
        while (low <= high) {
            val mid = (low + high).shr(1)
            val ore = requiredOre("FUEL", mid, reactions)
            when {
                ore == amount -> return mid
                ore < amount -> low = mid + 1
                ore > amount -> high = mid - 1
            }
        }
        return high
    }
}

fun main() {
    val reactions = Day14.parseInput()
    val minOre = Day14.requiredOre("FUEL", 1, reactions)
    println(minOre)

    println(Day14.maxFuelForOre(1000000000000, reactions))
}