package aoc2021

import util.replace
import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private object Day23 {

    val energyCosts = mapOf(
        'A' to 1,
        'B' to 10,
        'C' to 100,
        'D' to 1000
    )

    val roomPositions = mapOf(
        'A' to 2,
        'B' to 4,
        'C' to 6,
        'D' to 8
    )

    val validHallwayPositions = setOf(0, 1, 3, 5, 7, 9, 10)

    data class Room(
        val label: Char,
        val index: Int,
        val cells: List<Char>
    ) {
        fun isComplete() = cells.all { it == label }

        fun hasForeigners() = cells.any { it != '.' && it != label }
    }

    data class StateWithoutCost(
        val rooms: Map<Char, Room>,
        val hallway: List<Char>
    )

    data class State(
        val rooms: Map<Char, Room>,
        val hallway: List<Char>,
         val cost: Int = 0
    ): Comparable<State> {

        fun isComplete() = rooms.values.all { it.isComplete() }

        fun isHallwayClear(from: Int, to: Int): Boolean {
            return hallway.slice(min(from, to) .. max(from, to)).all { it == '.' }
        }

        fun roomsWithForeigners() = rooms.values.filter { room ->
            room.cells.any { it != '.' && it != room.label }
        }

        fun nextStates(): List<State> {
            val hallwaySlots = hallway.withIndex()
            val openHallwayPositions = hallwaySlots.filter { it.value == '.' && it.index in validHallwayPositions }
                .map { it.index }
            val hallwayCandidates = hallwaySlots.filter { it.value != '.' && !rooms[it.value]!!.hasForeigners() }

            return buildList {
                hallwayCandidates.forEach { (index, label) ->
                    val room = rooms[label]!!
                    val startIndex = if (index > room.index) index - 1 else index + 1
                    if (isHallwayClear(startIndex, room.index)) {
                        val depth = room.cells.lastIndexOf('.')
                        val moveCost = (depth + 1 + abs(index - room.index)) * energyCosts[label]!!
                        val newHallway = hallway.replace(index, '.')
                        val newRoom = Room(label, room.index, room.cells.replace(depth, label))
                        val newRooms = rooms + (label to newRoom)
                        add(State(newRooms, newHallway, cost + moveCost))
                    }
                }
                roomsWithForeigners().forEach { room ->
                    val (depth, label) = room.cells.withIndex().first { it.value != '.' }
                    openHallwayPositions.forEach { index ->
                        if (isHallwayClear(room.index, index)) {
                            val moveCost = (depth + 1 + abs(room.index - index)) * energyCosts[label]!!
                            val newRoom = Room(room.label, room.index, cells = room.cells.replace(depth, '.'))
                            val newRooms = rooms + (room.label to newRoom)
                            val newHallway = hallway.replace(index, label)
                             add(State(newRooms, newHallway, cost + moveCost))
                        }
                    }
                }
            }
        }

        fun withoutCost() = StateWithoutCost(rooms, hallway)

        override fun compareTo(other: State) = cost.compareTo(other.cost)
    }

    data class StateWithCost(
        val state: State,
        val cost: Int
    ) : Comparable<StateWithCost> {
        override fun compareTo(other: StateWithCost) = cost.compareTo(other.cost)
    }

    fun parseInput(input: List<String>): State {
        val hallway = input[1].drop(1).dropLast(1).toList()
        val rooms = roomPositions.map { (label, index) ->
            val cells = input.drop(2).dropLast(1).map { line ->
                line.drop(1).dropLast(1)[index]
            }
            label to Room(label, index, cells)
        }.toMap()
        return State(rooms, hallway)
    }

    fun searchStates(input: List<String>): Int {
        val initialState = parseInput(input)
        val q = PriorityQueue<State>().apply { add(initialState) }
        val currentCosts = mutableMapOf<StateWithoutCost, Int>().withDefault { Int.MAX_VALUE }
        while (q.isNotEmpty()) {
            val state = q.poll()
            if (state.isComplete()) {
                return state.cost
            }
            state.nextStates().forEach { nextState ->
                val stateWithoutCost = nextState.withoutCost()
                if (nextState.cost < currentCosts.getValue(stateWithoutCost)) {
                    currentCosts[stateWithoutCost] = nextState.cost
                    q.add(nextState)
                }
            }
        }
        throw Exception("goal not found")
    }

}

fun main() {
    val input = File("input.txt").readLines()
     println(Day23.searchStates(input))
    val extendedInput = input.take(3) + listOf(
        "  #D#C#B#A#  ",
        "  #D#B#A#C#  "
    ) + input.drop(3)
    println(Day23.searchStates(extendedInput))
}
