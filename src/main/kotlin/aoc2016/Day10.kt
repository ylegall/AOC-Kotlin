package aoc2016

import aoc2016.Day10.Recipient.Bot
import aoc2016.Day10.Recipient.Out
import util.input
import java.util.ArrayDeque

object Day10 {

    private sealed class Recipient(val id: String) {
        class Out(id: String): Recipient(id)
        class Bot(id: String): Recipient(id)
    }

    private class Robot(
            val low: Recipient,
            val high: Recipient
    )

    private fun parseRecipient(type: String, id: String) = when(type) {
        "bot"    -> Bot(id)
        "output" -> Out(id)
        else     -> throw IllegalStateException()
    }

    private fun parseRobot(tokens: List<String>) = Robot(
            parseRecipient(tokens[5], tokens[6]),
            parseRecipient(tokens[10], tokens[11])
    )

    private fun simulate(robots: Map<String, Robot>, robotChips: MutableMap<String, ArrayDeque<Int>>) {
        val outputs = mutableMapOf<String, Int>()

        outer@while (true) {
            for ((botId, chipQueue) in robotChips.filterValues { it.size >= 2 }) {
                val robot = robots.getValue(botId)
                val (lowChip, highChip) = chipQueue.pop().let {
                    Math.min(it, chipQueue.peek()) to Math.max(it, chipQueue.pop())
                }
                // part 1
                if (lowChip == 17 && highChip == 61) {
                    println("part1: $botId")
                }

                when (robot.low) {
                    is Out -> outputs[robot.low.id] = lowChip
                    is Bot -> robotChips.computeIfAbsent(robot.low.id) { ArrayDeque() }.add(lowChip)
                }
                when (robot.high) {
                    is Out -> outputs[robot.high.id] = highChip
                    is Bot -> robotChips.computeIfAbsent(robot.high.id) { ArrayDeque() }.add(highChip)
                }

                // part 2
                if (outputs["0"] != null && outputs["1"] != null && outputs["2"] != null) {
                    print("part2: ")
                    println(outputs["0"]!! * outputs["1"]!! * outputs["2"]!!)
                    break@outer
                }
            }
        }
    }

    fun run() {
        val robots = mutableMapOf<String, Robot>()
        val robotChips = mutableMapOf<String, ArrayDeque<Int>>()

        input("inputs/2016/10.txt").use { lines ->
            lines.forEach { line ->
                val tokens = line.split(" ")
                when (tokens.size) {
                    6    -> robotChips.computeIfAbsent(tokens[5]) { ArrayDeque() }.push(tokens[1].toInt())
                    12   -> robots[tokens[1]] = parseRobot(tokens)
                    else -> throw IllegalStateException()
                }
            }
        }

        simulate(robots, robotChips)
    }
}

fun main() {
    Day10.run()
}