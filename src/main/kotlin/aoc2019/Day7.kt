package aoc2019

import util.permutations
import java.util.ArrayDeque
import java.util.Deque

private object Day7 {

    private class Amp(
            private var initialValue: Int?,
            val output: Deque<Int> = ArrayDeque(),
            var input: () -> Int = { 0 }
    ) {
        fun read(): Int = if (initialValue != null) {
            val result = initialValue
            initialValue = null
            result
        } else {
            input()
        } ?: 0

        fun write(value: Int) {
            output.add(value)
        }
    }

    private fun initializeAmps(phases: List<Int>, loop: Boolean = false): List<Amp> {
        val amps = phases.map { Amp(it) }
        amps.zipWithNext().forEach { (prev, current) -> current.input = { prev.output.poll() } }
        if (loop) {
            amps.first().input = { amps.last().output.poll() }
        }
        return amps
    }

    fun runAmpLoop(codes: List<Int>, phases: Iterable<Int>, loop: Boolean = false): Int {
        return phases.permutations().map { phaseSequence ->
            println(phaseSequence)
            val amps = initializeAmps(phaseSequence)
            if (loop) amps.first().output.add(0)
            amps.forEach { amp ->
                IntCodeProcessor(
                        codes.toMutableList(),
//                        { amp.read().also { println("read $it") } },
//                        { amp.write(it.also { println("wrote $it") }) }
                        { amp.read() },
                        { amp.write(it) }
                ).run()
            }
            amps.last().output.peekLast()//.also { println(it) }
        }.max()!!
    }

    fun findLargestSignal(): Int {
        val codes = loadIntCodeInstructions("inputs/2019/7.txt").toList()
        return runAmpLoop(codes, 0 .. 4)
    }

    fun findLargestSignalWithLoop(): Int {
        val codes = loadIntCodeInstructions("inputs/2019/7.txt").toList()
        return runAmpLoop(codes, 5 .. 9, loop = true)
    }

}

fun main() {
//    println(Day7.findLargestSignal())
    println(Day7.findLargestSignalWithLoop())
}