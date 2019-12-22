package aoc2019

import util.Point
import java.util.Scanner

private object Day13 {

    private class TileGame(
            private val screenData: MutableMap<Point, Char> = HashMap()
    ) {

        private var x = 0
        private var y = 0
        private var outputMode = 0

        var score = 0; private set
        private val scanner = Scanner(System.`in`)
        var ballPosition: Int = 0; private set
        var paddlePosition: Int = 0; private set

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
        }

        private fun drawPixel(tileId: Int) {
            val tile = getTileType(tileId)
            screenData[Point(x, y)] = tile
            when (tile) {
                '*' -> ballPosition = x
                '-' -> paddlePosition = x
            }
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
            'a' -> -1
            'd' -> 1
            else -> 0
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

    fun countBlockTiles(codes: Iterable<Long>) {
        val tileGame = TileGame()
        val processor = intCodeProcessor(codes) {
            outputConsumer = { tileGame.write(it.toInt()) }
        }
        processor.run()
        println(tileGame.countBlockTiles())
    }

    fun finalScore(codes: Iterable<Long>) {
        val tileGame = TileGame()
        val processor = intCodeProcessor(codes) {
            outputConsumer = { tileGame.write(it.toInt()) }
            inputSupplier = {
                //System.`in`.read()
                //tileGame.print()
                //tileGame.readJoystick()
                tileGame.ballPosition.compareTo(tileGame.paddlePosition).toLong()
            }
            setMemoryValues(0 to 2)
        }
        processor.run()
        println(tileGame.score)
    }
}

fun main() {
    val codes = loadIntCodeInstructions("inputs/2019/13.txt")
    Day13.countBlockTiles(codes)
    Day13.finalScore(codes)
}