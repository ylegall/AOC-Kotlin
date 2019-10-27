package aoc2017

import java.io.File
import java.util.*
import kotlin.collections.HashMap

class SoundMachine(
        private val instructions: List<List<String>>
) {
    private val regs = HashMap<Char, Long>()
    private var ip = 0L
    private var snd = 0L
    var rcv = 0L; private set

    private fun String?.symbolValue() = this?.let {
        if (this[0] in 'a'..'z') {
            regs[this[0]] ?: 0
        } else {
            this.toLong()
        }
    } ?: 0

    private fun exec(op: String, x: String, y: String?) {
        // println("$ip: $op $x $y")
        val value = y.symbolValue()
        val src = x.symbolValue()
        var jump = 1L

        when(op) {
            "set" -> regs[x[0]] = value
            "add" -> regs[x[0]] = src + value
            "mul" -> regs[x[0]] = src * value
            "mod" -> regs[x[0]] = src % value
            "jgz" -> if (src > 0) jump = value
            "snd" -> snd = src
            "rcv" -> if (src != 0L) rcv = snd
            else -> throw Exception("invalid op: $op")
        }
        ip += jump
    }

    fun exec() {
        while (ip in instructions.indices && rcv == 0L) {
            val ins = instructions[ip.toInt()]
            exec(ins[0], ins[1], ins.getOrNull(2))
        }
    }
}


class SendReceiveMachine(
        private val instructions: List<List<String>>,
        private val sendQueue: Queue<Long>,
        private val receiveQueue: Queue<Long>,
        id: Long
) {
    private val regs = HashMap<Char, Long>().apply { put('p', id) }
    var sendCount = 0L; private set
    private var ip = 0L

    private fun String?.symbolValue() = this?.let {
        if (this[0] in 'a'..'z') {
            regs[this[0]] ?: 0
        } else {
            this.toLong()
        }
    } ?: 0

    fun isBlocked() = instructions[ip.toInt()][0] == "rcv" && receiveQueue.isEmpty()

    private fun exec(op: String, x: String, y: String?) {
        //println("$ip: $op $x $y")
        val value = y.symbolValue()
        val src = x.symbolValue()
        var jump = 1L

        when(op) {
            "set" -> regs[x[0]] = value
            "add" -> regs[x[0]] = src + value
            "mul" -> regs[x[0]] = src * value
            "mod" -> regs[x[0]] = src % value
            "jgz" -> if (src > 0) jump = value
            "snd" -> sendQueue.offer(src).also { sendCount++ }//.also { println("sending $src") }
            "rcv" -> {
                if (receiveQueue.isNotEmpty()) {
                    regs[x[0]] = receiveQueue.remove()
                    //println("received ${regs[x[0]]}")
                } else {
                    return
                }
            }
            else -> throw Exception("invalid op: $op")
        }
        ip += jump
    }

    fun exec() {
        while (ip in instructions.indices) {
            val ins = instructions[ip.toInt()]
            exec(ins[0], ins[1], ins.getOrNull(2))
            if (isBlocked()) break
        }
    }
}

private fun findFirstReceive(instructions: List<List<String>>) {
    val machine = SoundMachine(instructions)
    machine.exec()
    println(machine.rcv)
}

private fun sendCount(instructions: List<List<String>>) {
    val q01 = ArrayDeque<Long>()
    val q10 = ArrayDeque<Long>()
    val program0 = SendReceiveMachine(instructions, q01, q10, id = 0)
    val program1 = SendReceiveMachine(instructions, q10, q01, id = 1)

    while (true) {
        program0.exec()
        program1.exec()
        program0.exec()
        if (program0.isBlocked() && program1.isBlocked()) break
    }

    println(program0.sendCount)
    println(program1.sendCount)
}

fun main() {
    val instructions = File("inputs/2017/18.txt").readLines().map { it.split(" ") }
    findFirstReceive(instructions)
    sendCount(instructions)
}
