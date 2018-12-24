package aoc2018

import util.input
import java.util.*

private fun sortTasks(tasks: MutableMap<Char, MutableSet<Char>>) {
    val sortedTasks = arrayListOf<Char>()
    while (tasks.isNotEmpty()) {
        tasks.entries.sortedBy {
            it.key
        }.find {
            it.value.isEmpty()
        }?.key?.let {
            sortedTasks.add(it)
            tasks.entries.forEach { entry -> entry.value.remove(it) }
            tasks.remove(it)
        }
    }
    println(sortedTasks.joinToString(""))
}

const val NUM_WORKERS = 5

data class Task(val label: Char, val time: Int)

private fun taskTime(c: Char) = 61 + (c - 'A')

private fun timeTasks(tasks: MutableMap<Char, MutableSet<Char>>) {
    var currentTime = 0
    val queue = PriorityQueue<Task>(10) { t1, t2 -> t1.time.compareTo(t2.time)}

    while (tasks.isNotEmpty() || queue.isNotEmpty()) {
        tasks.entries.filter {
            it.value.isEmpty()
        }.sortedBy {
            it.key
        }.take(NUM_WORKERS - queue.size).forEach {
            queue.add(Task(it.key, currentTime + taskTime(it.key)))
            tasks.remove(it.key)
        }

        println(queue)

        if (queue.isNotEmpty()) {
            val completedTask = queue.remove()
            currentTime = completedTask.time
            println("finished task ${completedTask.label} at time $currentTime")
            tasks.entries.forEach { entry -> entry.value.remove(completedTask.label) }
        }
    }
    println(currentTime)
}

fun main() {
    input("inputs/2018/7.txt").use { lines ->
        val tasks = hashMapOf<Char, MutableSet<Char>>()
        for (line in lines) {
            val tokens = line.split(" ")
            tasks.getOrPut(tokens[1][0]) { hashSetOf() }
            tasks.getOrPut(tokens[7][0]) { hashSetOf() }.add(tokens[1][0])
        }
        println(tasks)
        sortTasks(tasks)
        timeTasks(tasks)
    }
}