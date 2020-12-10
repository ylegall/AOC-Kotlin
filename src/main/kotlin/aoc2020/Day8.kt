package aoc2020

import util.replace
import java.io.File


private object Day8 {

    class Interpreter(val code: List<String>) {
        var pc = 0
        var acc = 0L
        var trace = false

        // 426
        fun runWithLoopProtection(): Map<String, String> {
            val seenLines = mutableSetOf<Int>()
            while (true) {
                if (pc in seenLines) {
                    if (trace) println("infinite loop")
                    break
                }
                if (pc >= code.size) {
                    if (trace) println("normal exit")
                    break
                }
                seenLines.add(pc)
                execOp(code[pc])
            }

            return mapOf(
                    "pc" to pc.toString(),
                    "acc" to acc.toString()
            )
        }

        fun execOp(line: String) {
            if (trace) println("$pc: $line")
            val tokens = line.split(' ')
            when (tokens[0]) {
                "nop" -> {
                    pc++
                }
                "acc" -> {
                    acc += tokens[1].toLong()
                    pc++
                }
                "jmp" -> {
                    pc += tokens[1].toInt()
                }
            }
        }
    }

    fun findSubstitution(code: List<String>) {
        for (i in code.indices) {
            val modifiedCode = when {
                code[i].startsWith("nop") -> code.replace(i, code[i].replace("nop", "jmp"))
                code[i].startsWith("jmp") -> code.replace(i, code[i].replace("jmp", "nop"))
                else                      -> code
            }
            val vm = Interpreter(modifiedCode)
            val result = vm.runWithLoopProtection()
            if (result["pc"]!!.toInt() >= modifiedCode.size) {
                println("modified line $i (${code[i]}) to (${modifiedCode[i]})")
                println(result)
                return
            }
        }
        println("no substitutions found")
    }

}

fun main() {
    val input = File("inputs/2020/8.txt").readLines()
    val vm = Day8.Interpreter(input)
    //vm.trace = true
    println(vm.runWithLoopProtection())
    Day8.findSubstitution(input)
}