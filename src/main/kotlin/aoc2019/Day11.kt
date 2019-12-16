package aoc2019

import util.Direction
import util.Point
import util.enclosingRect
import util.move

private object Day11 {

    private class HullPaintBot(
            paintStartPanel: Boolean = false
    ) {
        private var pos = Point(0, 0)
        private var dir = Direction.NORTH
        private var inputState = 0
        val whitePanels = HashSet<Point>()
        val allPaintedPositions = HashSet<Point>()

        init {
            if (paintStartPanel) {
                whitePanels.add(pos)
            }
        }

        fun isPanelPainted() = pos in whitePanels

        fun input(signal: Long) {
            when (inputState) {
                0 -> paintPanel(signal)
                1 -> turnAndMove(signal)
            }
            inputState = (inputState + 1) % 2
        }

        fun paintPanel(color: Long) {
            if (color == 0L) {
                whitePanels.remove(pos)
            } else {
                whitePanels.add(pos)
            }
            allPaintedPositions.add(pos)
        }

        fun turnAndMove(turnFlag: Long) {
            dir = if (turnFlag == 0L) {
                dir.turnLeft()
            } else {
                dir.turnRight()
            }
            pos = pos.move(dir)
        }
    }

    fun paintHull(paintStartPanel: Boolean = false) {
        val codes = loadIntCodeInstructions("inputs/2019/11.txt")
        val bot = HullPaintBot(paintStartPanel)

        val processor = IntCodeProcessor(
                codes,
                { if (bot.isPanelPainted()) 1 else 0 },
                { bot.input(it) }
        )
        processor.run()
        println("painted panels: " + bot.allPaintedPositions.size)

        if (paintStartPanel) {
            val (minPoint, maxPoint) = enclosingRect(bot.allPaintedPositions)
            val image = (minPoint.y .. maxPoint.y).joinToString("\n") { y ->
                (minPoint.x .. maxPoint.x).joinToString("") { x ->
                    if (Point(x, y) in bot.whitePanels) "#" else " "
                }
            }
            println(image)
        }
    }

}

fun main() {
    Day11.paintHull()
    Day11.paintHull(true)
}