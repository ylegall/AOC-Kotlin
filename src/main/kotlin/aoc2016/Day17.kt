package aoc2016

import util.toHexString
import java.security.MessageDigest
import java.util.*


private object Day17 {

    private const val INPUT = "ioramepc"
    private val md = MessageDigest.getInstance("MD5")
    private val openChars = "bcdef".toSet()

    private data class Pos(
            val row: Int,
            val col: Int,
            val path: String = ""
    ) {
        fun neighbors(): List<Pos> {
            val hashOfPath = hash(path).take(4).toList()
            return listOf(
                    Pos(row - 1, col, path + "U"),
                    Pos(row + 1, col, path + "D"),
                    Pos(row, col - 1, path + "L"),
                    Pos(row, col + 1, path + "R")
            ).zip(hashOfPath).filter { (pos, _) ->
                pos.row in 0..3 && pos.col in 0..3
            }.filter { (_, char) ->
                char in openChars
            }.map { it.first }
        }
        fun isGoal() = this.row == 3 && this.col == 3
    }

    private fun hash(str: String): String {
        md.reset()
        md.update((INPUT + str).toByteArray())
        return md.digest().toHexString()
    }


    fun run(): String {
        var longestPath = Pos(0, 0)
        val positionQueue = ArrayDeque<Pos>().apply { add(Pos(0, 0)) }

        while (positionQueue.isNotEmpty()) {
            val nextPos = positionQueue.poll()
            if (nextPos.isGoal()) {
                if (nextPos.path.length > longestPath.path.length) {
                    longestPath = nextPos
                }
            } else {
                positionQueue.addAll(nextPos.neighbors())
            }
        }
        return longestPath.path
    }
}

fun main() {
    println(Day17.run().also { println(it.length) })
}