package aoc2018

import java.io.File
import java.util.*

private object Day7 {

    private const val NUM_WORKERS = 5

    private data class Task(val label: Char, val time: Int)

    private fun taskTime(c: Char) = 61 + (c - 'A')

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

            if (queue.isNotEmpty()) {
                val completedTask = queue.remove()
                currentTime = completedTask.time
                tasks.entries.forEach { entry -> entry.value.remove(completedTask.label) }
            }
        }
        println(currentTime)
    }

    fun parseInput(): MutableMap<Char, MutableSet<Char>> {
        val lines = File("inputs/2018/7.txt").readLines()
        val tasks = hashMapOf<Char, MutableSet<Char>>()
        for (line in lines) {
            val tokens = line.split(" ")
            tasks.getOrPut(tokens[1][0]) { hashSetOf() }
            tasks.getOrPut(tokens[7][0]) { hashSetOf() }.add(tokens[1][0])
        }
        return tasks
    }

    fun run() {
        sortTasks(parseInput())
        timeTasks(parseInput())
    }
}


fun main() {
    Day7.run()
}