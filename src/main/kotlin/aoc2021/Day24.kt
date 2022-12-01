package aoc2021

import util.arrayDequeOf
import java.io.File
import java.lang.IllegalArgumentException

fun main() {

    fun base26(x: Long): String {
        var x = x
        val digits = mutableListOf<Long>()
        while (x > 0L) {
            digits.add(x % 26L)
            x /= 26L
        }
        return digits.reversed().toString()
    }

    class Processor {

        val memory = "wxyz".associate { it to 0L }.toMutableMap()
        val inputBuffer = arrayDequeOf<Int>()
        var debug = false

        fun addInput(str: String) {
            for (c in str) {
                inputBuffer.offer(Character.digit(c, 10))
            }
        }

        fun eval(program: List<String>) {
            for (i in program.indices) {
                val line = program[i]
                line.trim()
                if (line.isEmpty()) continue
                if (debug) println(line)
                eval(line)
                if (debug) printMemory()
            }
        }

        fun eval(op: String) {
            val ops = op.split(" ")
            val dst = ops[1][0]
            val value = ops.getOrNull(2)?.let { src ->
                if (src[0] in 'w'..'z') {
                    memory[src[0]]
                } else {
                    src.toLong()
                }
            } ?: 0

            when (ops[0]) {
                "inp" -> {
                    // memory[dst] = readln().toInt()
                    check(inputBuffer.isNotEmpty()) { "no more input" }
                    memory[dst] = inputBuffer.poll().toLong()
                }
                "add" -> {
                    memory[dst] = memory[dst]!! + value
                }
                "mul" -> {
                    memory[dst] = memory[dst]!! * value
                }
                "div" -> {
                    memory[dst] = memory[dst]!! / value
                }
                "mod" -> {
                    memory[dst] = memory[dst]!! % value
                }
                "eql" -> {
                    memory[dst] = if (memory[dst] == value) 1 else 0
                }
                else -> throw IllegalArgumentException("bad op: '$op'")
            }
        }

        fun printMemory() {
            val zb26 = base26(memory['z']!!)
            println("$memory, $zb26")
        }


    }

    fun runRange() {
        val validCodes = mutableListOf<String>()
//        val program = File("inputs/2021/input-small.txt").readLines()
        val program = File("inputs/2021/input.txt").readLines()
        // 9,  9,  6,   1
        // 10, 13, -12, -13
        // 5,  9,  4,   14
//        for (i in 1111L .. 9999L) {
        for (i in 81914111111111L .. 99999692496829) {
//        for (i in 99919692496829L .. 99999692496829) {
            val code = i.toString()
            if ('0' in code) continue

            val processor = Processor()
            processor.inputBuffer.clear()
            processor.addInput(code)
            processor.eval(program)
            if (processor.memory['z'] == 0L) {
                validCodes.add(code)
                println("valid code: $code")
                val zb26 = processor.memory['z']!!.toString(26)
                println("$code: ${processor.memory}, $zb26")
            }

        }
        println(validCodes)
    }

    fun runSingle(code: String) {
        println(code)
        //val program = File("inputs/2021/input-small.txt").readLines()
        val program = File("inputs/2021/input.txt").readLines()
        val processor = Processor()
        processor.debug = true
        processor.addInput(code)
        processor.eval(program)
        println(code)
        processor.printMemory()
    }

    fun part1() {
        //runSingle("9961")
//        runSingle("99919692496829")
         runRange()
    }

    part1()
}