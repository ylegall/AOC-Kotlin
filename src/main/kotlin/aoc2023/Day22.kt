package aoc2023

import util.Point
import java.io.File


fun main() {

    data class Coord(
        val x: Int, val y: Int, val z: Int
    ) {
        operator fun plus(other: Coord) = Coord(x + other.x, y + other.y, z + other.z)
        fun xy() = Point(x, y)
    }

    data class Block(
        val id: Int,
        val low: Coord,
        val high: Coord
    ) {
        fun allCoords(): Iterable<Coord> {
            return when {
                low.x != high.x -> (low.x .. high.x).map { Coord(it, low.y, low.z) }
                low.y != high.y -> (low.y .. high.y).map { Coord(low.x, it, low.z) }
                else -> (low.z .. high.z).map { Coord(low.x, low.y, it) }
            }
        }

        fun movedBy(dir: Coord) = Block(id, low + dir, high + dir)
    }

    class BlockStack(
        val blocks: List<Block>,
        val supportMap: Map<Block, List<Block>>
    )

    fun parseInput(filename: String) = File(filename).readLines()
        .mapIndexed { id, line ->
            val tokens = line.split(",", "~").map { it.toInt() }
            val c1 = Coord(tokens[0], tokens[1], tokens[2])
            val c2 = Coord(tokens[3], tokens[4], tokens[5])
            Block(id, c1, c2)
        }.sortedBy {
            it.low.z
        }

    fun collapse(blocks: Iterable<Block>): BlockStack {
        val maxBlocksAtXy = mutableMapOf<Point, Block>()
        val supportingBlocks = mutableMapOf<Block, List<Block>>()
        val fallen = blocks.map { block ->
            val blocksBelow = block.allCoords().mapNotNull { coord -> maxBlocksAtXy[coord.xy()] }
            val supporting = blocksBelow.groupBy { it.high.z }.maxByOrNull { it.key }
            val newZ = 1 + (supporting?.key ?: 0)

            val newBlock = block.movedBy(Coord(0, 0, newZ - block.low.z))
            newBlock.allCoords().forEach { maxBlocksAtXy[it.xy()] = newBlock }

            if (supporting != null) {
                supportingBlocks[newBlock] = supporting.value.distinct()
            }

            newBlock
        }
        return BlockStack(fallen, supportingBlocks)
    }

    fun part1() {
        val stack = collapse(parseInput("input.txt"))
        val unsafeBlocks = stack.blocks.mapNotNull { block ->
            stack.supportMap[block]?.takeIf { it.size == 1 }?.firstOrNull()
        }.toSet()

        val safeBlocks = stack.blocks.filter { it !in unsafeBlocks }

        println(safeBlocks.size)
    }

    fun part2() {
        val stack = collapse(parseInput("input.txt"))
        val unsafeBlocks = stack.blocks.mapNotNull { block ->
            stack.supportMap[block]?.takeIf { it.size == 1 }?.firstOrNull()
        }.toSet()

        val originalBlocks = stack.blocks.toSet()

        val result = unsafeBlocks.sumOf { block ->
            val newBlocks = collapse(originalBlocks - block)
            newBlocks.blocks.count { it !in originalBlocks }
        }
        println(result)
    }

    part1()
    part2()
}
