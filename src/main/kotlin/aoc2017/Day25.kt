package aoc2017

import java.util.ArrayDeque

private object Day25 {

    private const val CHECKSUM_STEPS = 12368930

    private class Tape (val defaultVal: Int = 0) {
        val leftTape = ArrayDeque<Int>()
        val rightTape = ArrayDeque<Int>()
        var currentHead = defaultVal

        fun read(): Int {
            return currentHead
        }

        fun write(value: Int) {
            currentHead = value
        }

        fun moveHeadLeft() {
            rightTape.push(currentHead)
            if (leftTape.isEmpty()) {
                leftTape.push(defaultVal)
            }
            currentHead = leftTape.pop()
        }

        fun moveHeadRight() {
            leftTape.push(currentHead)
            if (rightTape.isEmpty()) {
                rightTape.push(defaultVal)
            }
            currentHead = rightTape.pop()
        }

        fun update(action: Action): Char {
            write(action.write)
            when(action.direction) {
                'L' -> moveHeadLeft()
                'R' -> moveHeadRight()
            }
            return action.state
        }

        fun checkSum() = currentHead + leftTape.sum() + rightTape.sum()
    }

    private class Action(val write: Int, val direction: Char, val state: Char)

    private class TuringMachine {

        val tape = Tape()
        var state = 'A'
        var counter = 0

        fun update() {
            val action = when (Pair(state, tape.read())) {
                'A' to 0 -> Action(1, 'R', 'B')
                'A' to 1 -> Action(0, 'R', 'C')
                'B' to 0 -> Action(0, 'L', 'A')
                'B' to 1 -> Action(0, 'R', 'D')
                'C' to 0 -> Action(1, 'R', 'D')
                'C' to 1 -> Action(1, 'R', 'A')
                'D' to 0 -> Action(1, 'L', 'E')
                'D' to 1 -> Action(0, 'L', 'D')
                'E' to 0 -> Action(1, 'R', 'F')
                'E' to 1 -> Action(1, 'L', 'B')
                'F' to 0 -> Action(1, 'R', 'A')
                'F' to 1 -> Action(1, 'R', 'E')
                else -> throw IllegalArgumentException()
            }
            state = tape.update(action)
            counter++
        }

    }

    fun runUntilDone(): Int {
        val turingMachine = TuringMachine()
        while (turingMachine.counter < CHECKSUM_STEPS) {
            turingMachine.update()
        }
        return turingMachine.tape.checkSum()
    }

}

fun main() {
    println(Day25.runUntilDone())
}