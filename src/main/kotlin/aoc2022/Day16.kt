package aoc2022

import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis


fun main() {

    class Input(
        val flowRates: Map<String, Int>,
        val nextValves: Map<String, List<String>>
    )

    fun parseInput(inputPath: String): Input {
        val inputPattern = """Valve (\w\w) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()
        val flowRates = mutableMapOf<String, Int>()
        val nextValves = mutableMapOf<String, List<String>>()
        File(inputPath).readLines().forEach { line ->
            val (valve, rate, next) = inputPattern.matchEntire(line)?.destructured!!
            flowRates[valve] = rate.toInt()
            nextValves[valve] = next.split(", ")
        }
        return Input(flowRates, nextValves)
    }

    val input = parseInput("input.txt")

    fun findShortestPaths(start: String): Map<String, Int> {
        val q = PriorityQueue<Pair<String, Int>> { p1, p2 -> p1.second.compareTo(p2.second) }
        q.add(start to 0)
        val seen = mutableSetOf<String>()
        val costs = mutableMapOf<String, Int>()
        while (q.isNotEmpty()) {
            val (pos, steps) = q.poll()
            costs[pos] = min(costs[pos] ?: steps, steps)
            seen.add(pos)
            val nextMoves = input.nextValves[pos]!!
                .filter { it !in seen }
                .map { it to (steps + 1) }
            q.addAll(nextMoves)
        }
        return costs
    }

    val valvesToOpen = input.flowRates.entries.filter { it.value > 0 }.map { it.key }.toSet()
    val allShortestPaths = input.flowRates.keys.associateWith { findShortestPaths(it) }

    data class State(
        val valve: String,
        val time: Int = 0,
        val openValves: Map<String, Int> = emptyMap()
    )

    fun calculateFlow(openedValves: Map<String, Int>, totalTime: Int): Int {
        return openedValves.entries.sumOf { (valve, openTime) ->
            val flowRate = input.flowRates[valve]!!
            (totalTime - openTime) * flowRate
        }
    }

    fun dfs(state: State): Int {
        val nextValves = valvesToOpen.filter { it !in state.openValves.keys }
        var maxFlow = calculateFlow(state.openValves, 30)
        for (nextValve in nextValves) {
            val travelCost = allShortestPaths[state.valve]!![nextValve]!!
            if (travelCost + 1 + state.time > 30) continue
            val newTime = state.time + travelCost + 1
            val newOpenedValves = state.openValves + Pair(nextValve, newTime)
            val newState = State(nextValve, newTime, newOpenedValves)
            val result = dfs(newState)
            maxFlow = max(maxFlow, result)
        }
        return maxFlow
    }

    data class State2(
        val valve1: String,
        val valve2: String,
        val time1: Int = 0,
        val time2: Int = 0,
        val openValves: Map<String, Int> = emptyMap()
    )

    fun dfsWithElephant(state: State2): Int {
        val nextValves = valvesToOpen.filter { it !in state.openValves.keys }
        var maxFlow = calculateFlow(state.openValves, 26)
        for (nextValve in nextValves) {
            val result = if (state.time1 < state.time2) {
                val travelCost = allShortestPaths[state.valve1]!![nextValve]!!
                if (travelCost + 1 + state.time1 > 26) continue
                val newTime1 = state.time1 + travelCost + 1
                val newOpenedValves = state.openValves + Pair(nextValve, newTime1)
                val newState2 = State2(nextValve, state.valve2, newTime1, state.time2, newOpenedValves)
                dfsWithElephant(newState2)
            } else {
                val travelCost = allShortestPaths[state.valve2]!![nextValve]!!
                if (travelCost + 1 + state.time2 > 26) continue
                val newTime2 = state.time2 + travelCost + 1
                val newOpenedValves = state.openValves + Pair(nextValve, newTime2)
                val newState2 = State2(state.valve1, nextValve, state.time1, newTime2, newOpenedValves)
                dfsWithElephant(newState2)
            }
            maxFlow = max(maxFlow, result)
        }
        return maxFlow
    }

    val elapsed1 = measureTimeMillis {
        println(dfs(State("AA")))
    }
    println("elapsed ms: $elapsed1")

    val elapsed2 = measureTimeMillis {
        println(dfsWithElephant(State2("AA", "AA")))
    }
    println("elapsed ms: $elapsed2")
}