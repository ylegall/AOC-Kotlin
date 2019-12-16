package aoc2019

import util.Point
import java.util.Scanner

private object Day13 {

    private class TileGame {

        private var x = 0
        private var y = 0
        private var outputMode = 0
        private val screenData = HashMap<Point, Char>()
        private var score = 0
        private val scanner = Scanner(System.`in`)

        fun write(value: Int) {
            when (outputMode) {
                0 -> x = value
                1 -> y = value
                2 -> if (x == -1 && y == 0) updateScore(value) else drawPixel(value)
            }
            outputMode = (outputMode + 1) % 3
        }

        private fun updateScore(value: Int) {
            score = value
            println("score: $score")
        }

        private fun drawPixel(tileId: Int) {
            screenData[Point(x, y)] = getTileType(tileId)
        }

        private fun getTileType(tileId: Int) = when (tileId) {
            0 -> ' '
            1 -> '|'
            2 -> '#'
            3 -> '-'
            4 -> '*'
            else -> throw Exception("invalid tile id: $tileId")
        }

        fun readJoystick() = when (scanner.nextLine().getOrElse(0) { 's' }) {
            'a' -> -1L
            'd' -> 1L
            else -> 0L
        }

        fun countBlockTiles() = screenData.entries.count { it.value == '#' }

        fun print() {
            for (x in 0 until 22) {
                for (y in 0 until 50) {
                    print(screenData[Point(y, x)] ?: ' ')
                }
                println()
            }
        }
    }

    fun countBlockTiles(codes: MutableList<Long>) {
        val tileGame = TileGame()
        val processor = IntCodeProcessor(codes, outputConsumer = { tileGame.write(it.toInt()) })
        processor.run()
        println(tileGame.countBlockTiles())
    }

    fun finalScore(codes: MutableList<Long>) {
        val tileGame = TileGame()
        val processor = IntCodeProcessor(
                codes,
                outputConsumer = { tileGame.write(it.toInt()) },
                inputSupplier = { tileGame.print(); tileGame.readJoystick() }
        )
        processor.setMemoryValues(0 to 2)

        while (processor.state != ProcessorState.HALTED) {
            processor.run()
        }
    }
}

fun main() {
    val codes = loadIntCodeInstructions("inputs/2019/13.txt")
    //Day13.countBlockTiles(codes)
    Day13.finalScore(codes)
}