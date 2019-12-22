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
            amps.first().input = { amps.last().output.poll() ?: 0 }
        }
        return amps
    }

    fun runAmpLoop(codes: List<Long>, phases: Iterable<Int>, loop: Boolean = false): Int {
        return phases.permutations().map { phaseSequence ->
            val amps = initializeAmps(phaseSequence, loop)
            val processors = amps.map { amp ->
                intCodeProcessor(codes) {
                    inputSupplier = { amp.read().toLong() }
                    outputConsumer = { amp.write(it.toInt()); pause() }
                }
            }

            var i = 0
            while (processors.none { it.state == ProcessorState.HALTED }) {
                processors[i].run()
                i = (i + 1) % processors.size
            }
            amps.last().output.peekLast()

        }.max() ?: -1
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
    println(Day7.findLargestSignal())
    println(Day7.findLargestSignalWithLoop())
}