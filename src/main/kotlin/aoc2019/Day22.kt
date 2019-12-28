package aoc2019

import aoc2019.Day22.Shuffle.*
import java.io.File
import kotlin.math.abs

private object Day22 {

    private sealed class Shuffle {
        data class Cut(val n: Int): Shuffle()
        object Deal: Shuffle()
        data class DealWithIncrement(val n: Int): Shuffle()
    }

    private fun parseInput(): List<Shuffle> {
        return File("inputs/2019/22.txt").readLines().map { line ->
            when {
                line.startsWith("cut") -> {
                    val n = line.substring(line.lastIndexOf(' ') + 1).toInt()
                    Cut(n)
                }
                line.startsWith("deal with increment") -> {
                    val n = line.substring(line.lastIndexOf(' ') + 1).toInt()
                    DealWithIncrement(n)
                }
                else -> Deal
            }
        }
    }

    private class SmallDeck(size: Int) {
        var cards = List(size) { it }; private set

        fun deal() {
            cards = cards.reversed()
        }

        fun cut(n: Int) {
            val len = abs(n)
            cards = if (n < 0) {
                cards.takeLast(len) + cards.dropLast(len)
            } else {
                cards.drop(len) + cards.take(len)
            }
        }

        fun dealWithIncrement(n: Int) {
            val array = MutableList(cards.size) { 0 }
            var idx = 0
            for (item in cards) {
                array[idx] = item
                idx = (idx + n) % array.size
            }
            cards = array.toList()
        }
    }

    private class BigDeck(
            val size: Long,
            var indexToTrack: Long
    ) {
        fun cutReverse(n: Int) {
            indexToTrack = if (n < 0) {
                val m = abs(n)
                if (indexToTrack < m) {
                    indexToTrack + size - m
                } else {
                    indexToTrack - m
                }
            } else {
                if (indexToTrack >= size - n) {
                    indexToTrack - size + n
                } else {
                    indexToTrack + n
                }
            }
        }

        fun dealReverse() {
            indexToTrack = size - 1 - indexToTrack
        }

        fun dealWithIndexReverse(n: Int) {
            while (indexToTrack % n != 0L) {
                indexToTrack += size
            }
            indexToTrack /= n
        }
    }

    fun run() {
        val operations = parseInput()

        val smallDeck = SmallDeck(10007)
        operations.forEach { op ->
//            println("op = $op")
//            println("\t$i: ${smallDeck.cards.indexOf(2019)}")
            when (op) {
                is Cut               -> smallDeck.cut(op.n)
                is DealWithIncrement -> smallDeck.dealWithIncrement(op.n)
                Deal                 -> smallDeck.deal()
            }
        }
        println(smallDeck.cards.indices.find { smallDeck.cards[it] == 2019 })

//        val bigDeck = BigDeck(10007, 2306)
//        operations.reversed().forEachIndexed { i, op ->
////            println("op = $op")
//            when (op) {
//                is Cut               -> bigDeck.cutReverse(op.n)
//                is DealWithIncrement -> bigDeck.dealWithIndexReverse(op.n)
//                Deal                 -> bigDeck.dealReverse()
//            }
////            println("\t$i: ${bigDeck.indexToTrack}")
//        }
//        println(bigDeck.indexToTrack)

        val reversedOps = operations.reversed()
        val bigDeck = BigDeck(119315717514079, 2020)
        val seen = mutableSetOf(2020L)
//        val bigDeck = BigDeck(119315717514047, 2020)
        var cycles = 0
        while (true) {
            for (op in reversedOps) {
                when (op) {
                    is Cut               -> bigDeck.cutReverse(op.n)
                    is DealWithIncrement -> bigDeck.dealWithIndexReverse(op.n)
                    Deal                 -> bigDeck.dealReverse()
                }
            }
            cycles++
            println(bigDeck.indexToTrack)
            if (cycles >= 10) break
            if (bigDeck.indexToTrack in seen) {
                println("found repeat ${bigDeck.indexToTrack} after $cycles cycles")
                break
            }
            seen.add(bigDeck.indexToTrack)
        }

        //println(bigDeck.indexToTrack)
    }
}

fun main() {
    Day22.run()
}
