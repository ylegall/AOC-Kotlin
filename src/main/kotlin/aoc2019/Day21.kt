package aoc2019

import java.util.ArrayDeque


private object Day21 {

    private val codes = loadIntCodeInstructions("inputs/2019/21.txt")

    private fun executeScript(script: List<String>) {
        val inputQueue = ArrayDeque(script.joinToString("\n").toList())
        val processor = intCodeProcessor(codes) {
            inputSupplier = { inputQueue.poll().toLong() }
            outputConsumer = { print(if (it < 256) it.toChar() else it) }
        }
        processor.run()
        println()
    }

    fun run() {
        val script1 = listOf(
                "NOT C J",
                "AND D J",
                "NOT A T",
                "AND D T",
                "OR T J"
        )

//        val script2 = listOf(
//                "OR C T",
//                "OR E T",
//                "OR F T",
//                "NOT T T",
//                "AND D T",
//                "OR T J",
//
//                "NOT B T",
//                "NOT T T",
//                "OR C T",
//                "OR E T",
//                "NOT T T",
//                "AND D T",
//                "OR T J",
//
//                "NOT A T",
//                "OR T J"
//        )

        executeScript(script1 + "WALK\n")
//        executeScript(script2 + "RUN\n")
    }
}

fun main() {
    Day21.run()
}