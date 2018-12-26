package aoc2018

import util.Point
import util.input
import java.util.*
import kotlin.streams.toList

sealed class Actor(
        var pos: Point,
        var hp: Int = 200,
        var ap: Int = 3
)
class Elf(pos: Point): Actor(pos) {
    override fun toString() = "E(${pos.x},${pos.y})"
}
class Goblin(pos: Point): Actor(pos) {
    override fun toString() = "G(${pos.x},${pos.y})"
}

data class Path(
        val first: Point,
        val current: Point,
        val steps: Int
)

class Game(
        val grid: List<CharArray>,
        val elves: HashMap<Point, Elf>,
        val goblins: HashMap<Point, Goblin>
) {
    private fun cell(p: Point) = grid[p.y][p.x]

    private fun neighbors(p: Point): List<Point> {
        return listOf(
                Point(p.x, p.y - 1),
                Point(p.x - 1, p.y),
                Point(p.x + 1, p.y),
                Point(p.x, p.y + 1)
        ).filter { cell(it) != '#' }
    }

    private fun openNeighbors(p: Point) = neighbors(p).filter { cell(it) == '.' }

    private fun Actor.enemies() = if (this is Elf) goblins else elves

    private fun getDestination(actor: Actor): Point? {
        return actor.enemies().keys.flatMap { openNeighbors(it) }.sorted().firstOrNull()
    }

    private fun shortestPaths(src: Point, dst: Point): List<Path> {
        var minSteps = Int.MAX_VALUE
        val shortestPaths = hashSetOf<Path>()
        val q = PriorityQueue<Path>(compareBy({ it.steps }, { it.first }))
        val seen = hashSetOf(src)

        openNeighbors(src).forEach { point ->
            q.add(Path(point, point, 1))
        }

        while (q.isNotEmpty()) {
            val path = q.poll()
            if (path.current == dst) {
                if (path.steps < minSteps) {
                    shortestPaths.clear()
                    shortestPaths.add(path)
                    minSteps = path.steps
                } else if (path.steps == minSteps) {
                    shortestPaths.add(path)
                }
            } else {
                if (path.steps < minSteps) {
                    openNeighbors(path.current).filter { it !in seen }.forEach { next ->
                        q.add(Path(path.first, next, path.steps + 1))
                    }
                }
                seen.add(path.current)
            }
        }
        return shortestPaths.sortedBy { it.first }
    }

    fun run() {
        var round = 0
        while (elves.isNotEmpty() && goblins.isNotEmpty()) {
            // sort to find the turn order
            val actors = (goblins + elves).entries.sortedBy { it.key }.map { it.value }
            //println(actors)

            for (actor in actors) {
                val enemies = if (actor is Elf) goblins else elves
                val destination = getDestination(actor) ?: continue
                val paths = shortestPaths(actor.pos, destination)
                println(actor)
                println(paths)
                break
            }

            break
            round += 1
        }
        println("rounds: $round")
    }
}

private fun debug(grid: List<CharArray>) {
    for (y in 0 until grid.size) {
        for (x in 0 until grid[y].size) {
            print(grid[y][x])
        }
        println()
    }
}

fun main() {
    val elves = HashMap<Point, Elf>()
    val goblins = HashMap<Point, Goblin>()

    val grid = input("inputs/2018/15.txt").use {
        val lines = it.toList().map { it.toCharArray() }
        for (y in 0 until lines.size) {
            for (x in 0 until lines[y].size) {
                val pos = Point(x, y)
                when (lines[y][x]) {
                    'E' -> elves[pos] = Elf(pos)
                    'G' -> goblins[pos] = Goblin(pos)
                }
            }
        }
        lines
    }

    Game(grid, elves, goblins).run()
}