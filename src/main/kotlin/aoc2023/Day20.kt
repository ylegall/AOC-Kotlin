package aoc2023

import aoc2023.Day20.BROADCASTER
import aoc2023.Day20.ModuleType
import aoc2023.Day20.ModuleType.*
import aoc2023.Day20.SignalType
import aoc2023.Day20.SignalType.HIGH
import aoc2023.Day20.SignalType.LOW
import util.MutableCounter
import util.product
import java.io.File

private object Day20 {
    const val BROADCASTER = "broadcaster"
    enum class SignalType { LOW, HIGH }
    enum class ModuleType { BROADCAST, FLIP_FLOP, CONJUNCTION }
}

fun main() {

    data class Module(
        val id: String,
        val type: ModuleType,
        val children: List<String>
    )

    data class Signal(
        val type: SignalType,
        val to: String,
        val from: String
    )

    class Circuit(
        val inputs: Map<String, List<String>>,
        val modules: Map<String, Module>,
    ) {
        val signalStates: MutableMap<String, SignalType> = mutableMapOf()
        val signalCounts = MutableCounter<SignalType>()
        val signalQueue = ArrayDeque<Signal>()
        val receivedSignals = mutableListOf<SignalType>()
        val sentSignals = mutableListOf<SignalType>()
        var debugId: String? = null

        fun sendSignal(module: Module, signalType: SignalType) {
            signalCounts.increment(signalType, module.children.size.toLong())
            signalQueue.addAll(module.children.map { childId ->
                Signal(signalType, to = childId, from = module.id)
            })
            if (module.id == debugId) {
                sentSignals.add(signalType)
            }
            signalStates[module.id] = signalType
        }

        fun simulate() {
            signalQueue.add(Signal(LOW, BROADCASTER, "button"))
            signalCounts.increment(LOW)

            while (signalQueue.isNotEmpty()) {
                val (signalType, toId, fromId) = signalQueue.removeFirst()
                val module = modules[toId]
                if (module?.id == debugId) {
                    receivedSignals.add(signalType)
                }
                if (module == null) continue

                when (module.type) {
                    BROADCAST -> {
                        sendSignal(module, signalType)
                    }
                    FLIP_FLOP -> {
                        if (signalType == LOW) {
                            when (signalStates[module.id]) {
                                HIGH -> sendSignal(module, LOW)
                                else -> sendSignal(module, HIGH)
                            }
                        }
                    }
                    CONJUNCTION -> {
                        val inputValues = inputs[module.id]?.map { inputId ->
                            if (inputId == fromId) signalType else (signalStates[inputId] ?: LOW)
                        }!!
                        if (inputValues.all { it == HIGH }) {
                            sendSignal(module, LOW)
                        } else {
                            sendSignal(module, HIGH)
                        }
                    }
                }
            }
        }

        fun reset() {
            signalStates.clear()
            signalCounts.clear()
            signalQueue.clear()
            receivedSignals.clear()
            sentSignals.clear()
        }
    }

    fun parseInput(filename: String): Circuit {
        val namePattern = Regex("""\w+""")
        val inputsList = mutableListOf<Pair<String, String>>()
        val modules = mutableMapOf<String, Module>()

        File(filename).readLines().map { line ->
            val type = if (line.startsWith(BROADCASTER)) {
                BROADCAST
            } else {
                if (line[0] == '%') FLIP_FLOP else CONJUNCTION
            }

            val tokens = namePattern.findAll(line).map { it.value }.toList()

            tokens.drop(1).map { it to tokens.first() }.let {
                inputsList.addAll(it)
            }

            modules[tokens.first()] = Module(
                id = tokens.first(),
                type = type,
                children = tokens.drop(1)
            )
        }

        return Circuit(
            inputs = inputsList.groupBy({ it.first }, { it.second }),
            modules = modules
        )
    }

    fun allPathsFromRoot(targetModule: String, circuit: Circuit): List<List<String>> {
        val allPaths = mutableListOf<List<String>>()

        fun recurse(id: String, path: List<String> = emptyList()) {
            if (id == targetModule) {
                allPaths.add(path)
            } else {
                circuit.modules[id]?.children
                    ?.filter { it !in path }
                    ?.forEach { recurse(it, path + it) }
            }
        }
        recurse(BROADCASTER, listOf(BROADCASTER))
        return allPaths
    }

    fun cycleOfFirstLowSignal(moduleId: String, circuit: Circuit): Long {
        circuit.reset()
        circuit.debugId = moduleId
        var i = 0L
        while (i < 10000) {
            circuit.simulate()
            i++
            if (LOW in circuit.receivedSignals) {
                return i
            }
            circuit.receivedSignals.clear()
        }
        return -1
    }

    fun part1() {
        val circuit = parseInput("input.txt")
        repeat(1000) {
            circuit.simulate()
        }
        println(circuit.signalCounts.values.product())
    }

    fun part2() {
        val circuit = parseInput("input.txt")
        val allPaths = allPathsFromRoot("rx", circuit)
        val lastModules = allPaths.map { it.takeLast(3).first() }.distinct()

        val firstLowSignal = lastModules.map { moduleId ->
            cycleOfFirstLowSignal(moduleId, circuit)
        }.product(1L)
        println(firstLowSignal)
    }

    part1()
    part2()
}