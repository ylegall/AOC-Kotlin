package aoc2017

import java.io.File

private object Day24 {

    private fun parseInput(): Set<Pair<Int, Int>> {
        return File("inputs/2017/24.txt").useLines { lines ->
            lines.map { line ->
                line.split("/").let { it[0].toInt() to it[1].toInt() }
            }.toSet()
        }
    }

    class Bridge(
            val used: Set<Pair<Int, Int>> = emptySet(),
            val last: Int = 0
    ) {
        fun strength() = used.sumBy { it.first + it.second }

        fun addPiece(piece: Pair<Int, Int>): Bridge {
            check(last == piece.first || last == piece.second)
            return Bridge(
                    used + piece,
                    if (last == piece.first) piece.second else piece.first
            )
        }
    }

    fun bestBridgeRecursive(
            bridge: Bridge,
            pieces: Set<Pair<Int, Int>>,
            bridgeComparator: Comparator<Bridge>
    ): Bridge {
        val nextPieces = pieces.filter { it !in bridge.used && (it.first == bridge.last || it.second == bridge.last) }
        return if (pieces.isEmpty()) {
            bridge
        } else {
            nextPieces.map {
                bestBridgeRecursive(bridge.addPiece(it), pieces - it, bridgeComparator)
            }.maxWithOrNull(bridgeComparator) ?: bridge
        }
    }

    private val strengthComparator: Comparator<Bridge> = compareBy { it.strength() }
    private val lengthComparator: Comparator<Bridge> = compareBy({ it.used.size }, { it.strength() })

    fun bestBridge() {
        val pieces = parseInput()
        val strongestBridge = bestBridgeRecursive(Bridge(), pieces, strengthComparator)
        val longestBridge = bestBridgeRecursive(Bridge(), pieces, lengthComparator)
        println(strongestBridge.strength())
        println(longestBridge.strength())
    }
}

fun main() {
    Day24.bestBridge()
}