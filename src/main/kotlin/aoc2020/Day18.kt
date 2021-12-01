package aoc2020

import util.arrayDequeOf
import java.io.File


private fun evalWithNoParens(tokens: List<String>): Long {
    var arg: Long? = null
    var op = ""
    for (token in tokens) {
        when (token) {
            "*", "+" -> op = token
            else     -> {
                arg = if (arg == null) {
                    token.toLong()
                } else {
                    when (op) {
                        "+" -> arg + token.toLong()
                        "*" -> arg * token.toLong()
                        else -> throw Exception("invalid op: $op")
                    }
                }
            }
        }
    }
    return arg!!
}

private fun evalWithNoParens2(tokens: List<String>): Long {
    val args = arrayDequeOf<Long>()
    val ops = arrayDequeOf<String>()
    for (token in tokens) {
        when (token) {
            "*", "+" -> ops.push(token)
            else     -> {
                if (ops.isEmpty()) {
                    args.push(token.toLong())
                } else {
                    when (ops.peek()) {
                        "+" -> {
                            args.push(args.pop() + token.toLong())
                            ops.pop()
                        }
                        "*" -> {
                            args.push(token.toLong())
                        }
                        else -> throw Exception("invalid op")
                    }
                }
            }
        }
    }
    return args.toList().reduce { acc, arg -> acc * arg }
}

private fun eval(line: String, precedence: Boolean = false): Long {

    val tokens = line.replace("("," ( ").replace(")", " ) ").split(" ").filter { it.isNotBlank() }
    var exp = mutableListOf<String>()
    val q = arrayDequeOf<MutableList<String>>()
    for (token in tokens) {
        when (token) {
            "("  -> {
                q.push(exp)
                exp = mutableListOf()
            }
            ")"  -> {
                val subExp = if (precedence) evalWithNoParens2(exp) else evalWithNoParens(exp)
                exp = q.pop()
                exp.add(subExp.toString())
            }
            else -> exp.add(token)
        }
    }
    return if (precedence) evalWithNoParens2(exp) else evalWithNoParens(exp)
}


fun main() {
    val input = File("inputs/2020/18.txt").readLines()
    println(input.map { eval(it) }.sum())
    println(input.map { eval(it, true) }.sum())
}
