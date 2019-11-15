package aoc2017

import java.util.ArrayDeque

private object Day25 {

    private const val CHECKSUM_STEPS = 12368930

    private class Tape {
        private val leftTape = ArrayDeque<Int>()
        private val rightTape = ArrayDeque<Int>()
        var head = 0; private set

        fun read() = head

        fun write(value: Int) {
            head = value
        }

        fun moveHeadLeft() {
            rightTape.push(head)
            if (leftTape.isEmpty()) {
                leftTape.push(0)
            }
            head = leftTape.pop()
        }

        fun moveHeadRight() {
            leftTape.push(head)
            if (rightTape.isEmpty()) {
                rightTape.push(0)
            }
            head = rightTape.pop()
        }

        fun update(action: Action): Char {
            write(action.writeValue)
            when(action.direction) {
                'L' -> moveHeadLeft()
                'R' -> moveHeadRight()
            }
            return action.nextState
        }

        fun checkSum() = head + leftTape.sum() + rightTape.sum()
    }

    private class Action(val writeValue: Int, val direction: Char, val nextState: Char)

    private class TuringMachine {

        val tape = Tape()
        var state = 'A'; private set
        var counter = 0; private set

        fun update() {
            val action = when (state to tape.read()) {
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

    fun runUntilChecksum(): Int {
        val turingMachine = TuringMachine()
        while (turingMachine.counter < CHECKSUM_STEPS) {
            turingMachine.update()
        }
        return turingMachine.tape.checkSum()
    }

}

fun main() {
    println(Day25.runUntilChecksum())
}