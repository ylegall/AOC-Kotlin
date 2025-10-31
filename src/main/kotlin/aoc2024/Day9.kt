package aoc2024

import java.io.File
import java.util.*

fun main() {

    data class Block(
        val id: Int,
        val start: Int,
        val size: Int
    ) {
        fun checksum(): Long {
            return (start until (start + size)).sumOf { it.toLong() * id }
        }

        fun split(): List<Block> {
            return List(size) { Block(id, start + it, 1) }
        }
    }

    val blockComparator = compareBy<Block> { it.start }

    fun parseInput(): List<Block> {
        var position = 0
        return File("input.txt").readText().mapIndexed { index, digit ->
            val size = digit.digitToInt()
            val id = if (index % 2 == 0) index/2 else -1
            val startPosition = position
            position += size
            Block(id, startPosition, size)
        }.filter { it.size > 0 }
    }

    val blocks = parseInput()

    fun checksum(blocks: List<Block>): Long {
        return blocks.sumOf { it.checksum() }
    }

    fun compact(inputBlocks: List<Block>): List<Block> {
        val usedBlocks = inputBlocks.filter { it.id >= 0 }
        val freeBlocks = inputBlocks.filter { it.id < 0 }.groupBy { it.size }
            .let { groups -> groups.mapValues {
                PriorityQueue(blockComparator).apply { addAll(it.value) }
            }}
            .toMutableMap()

        val results = mutableListOf<Block>()

        fun findEmptySlot(block: Block): Block? {
            val slot = (block.size .. 9)
                .asSequence()
                .mapNotNull { size -> freeBlocks[size] }
                .filter { it.isNotEmpty() }
                .map { it.peek() }
                .sortedBy { it.start }
                .firstOrNull { it.start < block.start }
            if (slot != null) {
                freeBlocks[slot.size]!!.remove()
            }
            return slot
        }

        for (block in usedBlocks.reversed()) {
            val openSlot = findEmptySlot(block)
            val newBlock = if (openSlot != null) {
                val remaining = openSlot.size - block.size
                if (remaining > 0) {
                    val newEmptyStart = openSlot.start + block.size
                    val newEmptyBlock = Block(-1, newEmptyStart, remaining)
                    val slots = freeBlocks.getOrPut(remaining) {
                        PriorityQueue(blockComparator)
                    }
                    slots.add(newEmptyBlock)
                }
                Block(block.id, openSlot.start, block.size)
            } else {
                block
            }
            results.add(newBlock)
        }

        val compactBlocks = results.sortedBy { it.start }
        return compactBlocks
    }

    fun part1() {
        val splitBlocks = blocks.flatMap { it.split() }
        val compactedBlocks = compact(splitBlocks)
        println(checksum(compactedBlocks))
    }

    fun part2() {
        val compactedBlocks = compact(blocks)
        println(checksum(compactedBlocks))
    }

    part1()
    part2()
}