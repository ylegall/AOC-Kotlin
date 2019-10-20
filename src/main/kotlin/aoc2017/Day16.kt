package aoc2017

import java.io.File
import java.util.*


private fun Deque<Char>.permute(commands: List<String>) = commands.forEach { permute(it) }

private fun Deque<Char>.permute(command: String) {
    val op = command[0]
    val operands = command.substring(1).split("/")
    when (op) {
        's' -> rotate(operands[0].toInt())
        'x' -> exchange(operands[0].toInt(), operands[1].toInt())
        'p' -> swap(operands[0][0], operands[1][0])
    }
}

private fun Deque<Char>.rotate(x: Int) = repeat(x) {
    addFirst(removeLast())
}

private fun Deque<Char>.swap(a: Char, b: Char) = repeat(size) {
    when(val c = pop()) {
        a -> addLast(b)
        b -> addLast(a)
        else -> addLast(c)
    }
}

private fun Deque<Char>.exchange(a: Int, b: Int) {
    val copy = this.toList()
    for (i in indices) {
        val c = pop()
        when (i) {
            a -> addLast(copy[b])
            b -> addLast(copy[a])
            else -> addLast(c)
        }
    }
}

fun main() {
    val seenPermutations = HashSet<String>()
    val programs = ArrayDeque<Char>()
    for (i in 0 .. 15) {
        programs.addLast('a' + i)
    }
    val commands = File("inputs/2017/16.txt").readText().trim().split(",")

    programs.permute(commands)

    programs.joinToString("").also {
        println(it)
        seenPermutations.add(it)
    }

    var i = 1
    while (true) {
        programs.permute(commands)
        val permutation = programs.joinToString("")
        println("$i: $permutation")
        if (permutation in seenPermutations) {
            break
        }
        seenPermutations.add(permutation)
        i++
    }

    println("cycle length: " + seenPermutations.size)
    println(1_000_000_000 % i - 1)
}