package aoc2019

import util.arrayDequeOf
import java.util.ArrayDeque
import java.util.Scanner
import kotlin.system.exitProcess

private object Day25 {

    private val codes = loadIntCodeInstructions("inputs/2019/25.txt")

    private val preloadedCommands = arrayDequeOf("north", "take dark matter", "east", "east", "take bowl of rice",
            "west", "south", "take dehydrated water", "north", "west", "north", "east", "south", "take antenna",
            "west", "take hypercube", "east", "north", "west", "north", "take manifold", "west", "take jam", "east",
            "east", "take candy cane", "west", "south", "south", "south", "west", "south", "west"
    )

    private val allItems = listOf("candy cane", "hypercube", "manifold", "dark matter",
            "jam", "bowl of rice", "antenna", "dehydrated water"
    )

    private fun String.expandCommand() = when(this) {
        "n" -> listOf("north")
        "e" -> listOf("east")
        "s" -> listOf("south")
        "w" -> listOf("west")
        "drop all" -> allItems.map { "drop $it" }
        "take all" -> allItems.map { "take $it" }
        else -> listOf(this)
    }

    fun exploreShipAuto() {
        val scanner = Scanner(System.`in`)
        val inputQueue = ArrayDeque<Int>()

        fun getNextCommand(): Long {
            if (inputQueue.isEmpty()) {
                val commands = if (preloadedCommands.isEmpty()) {
                    scanner.nextLine().expandCommand()
                } else {
                    listOf(preloadedCommands.poll())
                }
                val charCodes = commands.joinToString("\n").map { it.code }
                inputQueue.apply { addAll(charCodes); add(10) }
            }
            return inputQueue.poll().toLong()
        }

        intCodeProcessor(codes) {
            inputSupplier = { getNextCommand() }
            outputConsumer = { print(it.toInt().toChar()) }
        }.run()
    }

    fun exploreShipManual() {
        val scanner = Scanner(System.`in`)
        val commandHistory = mutableListOf<String>()
        val inputQueue = ArrayDeque<Int>()

        fun getNextCommand(): Long {
            if (inputQueue.isEmpty()) {

                val commands = scanner.nextLine().expandCommand()
                if (commands == listOf("stop")) {
                    println(commandHistory)
                    exitProcess(0)
                }
                commandHistory.addAll(commands)
                val charCodes = commands.joinToString("\n").map { it.code }
                inputQueue.apply { addAll(charCodes); add(10) }
            }
            return inputQueue.poll().toLong()
        }

        intCodeProcessor(codes) {
            inputSupplier = { getNextCommand() }
            outputConsumer = { print(it.toInt().toChar()) }
        }.run()
    }

}

fun main() {
    Day25.exploreShipAuto()
}

