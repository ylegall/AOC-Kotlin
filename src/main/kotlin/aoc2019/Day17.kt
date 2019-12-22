package aoc2019

import util.Direction
import util.Point
import util.move
import java.util.ArrayDeque
import kotlin.math.min

private object Day17 {

    private fun getCameraData(codes: Iterable<Long>): List<String> {
        val scanline = StringBuilder()
        val imageData = mutableListOf<String>()
        val processor = intCodeProcessor(codes) {
            outputConsumer = {
                if (it == 10L) {
                    imageData.add(scanline.toString())
                    scanline.clear()
                } else {
                    scanline.append(it.toChar())
                }
            }
        }
        processor.run()
        return imageData
    }

    private fun List<String>.get(point: Point): Char {
        val row = this.getOrNull(point.y)
        return row?.getOrNull(point.x) ?: '.'
    }

    private fun List<String>.isIntersection(point: Point) = (get(point) == '#').and(
            point.cardinalNeighbors().all { get(it) == '#' }
    )

    private fun displayImage(imageData: List<String>) = println(imageData.joinToString("\n"))

    private fun buildRawPath(cameraData: List<String>): String {

        var dir = Direction.NORTH
        var pos = cameraData.findPoint { it == '^' }!!

        fun nextTurn(): Char? {
            val availableMoves = Direction.values().filter { direction ->
                cameraData.get(pos.move(direction)) == '#'
            }
            return when {
                dir.turnLeft() in availableMoves -> 'L'
                dir.turnRight() in availableMoves -> 'R'
                else -> null
            }
        }

        fun canMoveForward() = cameraData.get(pos.move(dir)) == '#'

        val moves = StringBuilder()
        outer@while (true) {
            var strideLength = 0
            while (canMoveForward()) {
                pos = pos.move(dir)
                strideLength++
            }
            if (strideLength > 0) moves.append(strideLength).append(',')
            when (nextTurn()) {
                'L' -> {
                    dir = dir.turnLeft()
                    moves.append('L').append(',')
                }
                'R' -> {
                    dir = dir.turnRight()
                    moves.append('R').append(',')
                }
                else -> break@outer
            }
        }
        return moves.toString()
    }

    private data class SequencePartition(
            val mainRoutine: String,
            val routineMappings: Map<String, String>
    )

    private fun partitionSequence(
            rawSequence: String,
            mainRoutine: String = "",
            routineMappings: Map<String, String> = emptyMap()
    ): SequencePartition? {
        when {
            mainRoutine.length > 20 -> return null
            routineMappings.size > 3 -> return null
            routineMappings.keys.any { it.length > 20 } -> return null
            rawSequence.isEmpty() -> return SequencePartition(mainRoutine, routineMappings)
        }

        fun String.append(routine: String) = if (this.isEmpty()) {
            routine
        } else {
            "$this,$routine"
        }

        for (i in 1 .. min(20, rawSequence.length)) {
            if (rawSequence[i] == ',') {
                val prefix = rawSequence.take(i)
                val result = if (prefix in routineMappings) {
                    partitionSequence(rawSequence.drop(i + 1), mainRoutine.append(routineMappings[prefix]!!), routineMappings)
                } else {
                    val newRoutineName = ('A' + routineMappings.size).toString()
                    partitionSequence(rawSequence.drop(i + 1), mainRoutine.append(newRoutineName), routineMappings + (prefix to newRoutineName))
                }
                if (result != null) return result
            }
        }
        return null
    }

    inline fun Iterable<String>.findPoint(predicate: (Char) -> Boolean) = mapIndexed { y, row ->
        row.mapIndexed { x, item ->
            Point(x, y) to item
        }
    }.flatten().firstOrNull {
        predicate(it.second)
    }?.first

    fun sumOfAlignmentParameters(codes: Iterable<Long>) {
        val image = getCameraData(codes)
        displayImage(image)
        var alignmentParameterSum = 0
        for (y in image.indices) {
            for (x in image[y].indices) {
                if (image.isIntersection(Point(x, y))) {
                    alignmentParameterSum += x * y
                }
            }
        }
        println(alignmentParameterSum)
    }

    fun totalDustCollected(codes: Iterable<Long>) {
        val image = getCameraData(codes)
        //displayImage(image)
        val rawPath = buildRawPath(image)
        val partition = partitionSequence(rawPath)!!
        println(partition)
        val moveQueue = ArrayDeque<Int>()
        with (moveQueue) {
            addAll(partition.mainRoutine.map { it.toInt() })
            add(10)
            addAll(partition.routineMappings.entries.find { it.value == "A" }!!.key.map { it.toInt() })
            add(10)
            addAll(partition.routineMappings.entries.find { it.value == "B" }!!.key.map { it.toInt() })
            add(10)
            addAll(partition.routineMappings.entries.find { it.value == "C" }!!.key.map { it.toInt() })
            add(10)
            add('n'.toInt())
            add(10)
        }

        println(moveQueue)

        var totalDust = 0L
        val processor = intCodeProcessor(codes) {
            inputSupplier = { moveQueue.poll()?.toLong() ?: 0L }
            outputConsumer = { totalDust = it }
        }
        processor.setMemoryValues(0 to 2)

        processor.run()
        println("total dust: $totalDust")
    }

}

fun main() {
    val codes = loadIntCodeInstructions("inputs/2019/17.txt")
    Day17.sumOfAlignmentParameters(codes)
    Day17.totalDustCollected(codes)
}