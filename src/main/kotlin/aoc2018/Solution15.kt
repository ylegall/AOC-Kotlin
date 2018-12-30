package aoc2018

import util.Point
import util.input
import util.mDist
import java.util.*
import kotlin.streams.toList

enum class Team(val symbol: Char) {
    Goblin('G'),
    Elf('E');
}

data class Actor(
        val team: Team,
        var pos: Point,
        var hp: Int = 200,
        var ap: Int = 3
)

data class Path(
        val first: Point,
        val current: Point,
        val mDist: Int,
        val steps: Int
)

class Game(
        val grid: List<CharArray>,
        val actors: HashMap<Point, Actor>
) {
    private fun cell(p: Point) = grid[p.y][p.x]

    private fun Point.neighbors(): List<Point> {
        return listOf(
                Point(x, y - 1),
                Point(x - 1, y),
                Point(x + 1, y),
                Point(x, y + 1)
        ).filter { cell(it) != '#' }
    }

    private fun Point.openNeighbors() = neighbors().filter { cell(it) == '.' }

    private fun Actor.closestEnemy() = pos.neighbors().mapNotNull {
        actors[it]
    }.filter {
        it.team != team
    }.sortedWith(compareBy(
            { it.hp }, { it.pos })
    ).firstOrNull()

    private fun Actor.getDestination(points: List<Point>): Point? {
        return points.flatMap {
            it.openNeighbors()
        }.distinct().flatMap {
            shortestPaths(pos, it)
        }.groupBy {
            it.steps
        }.minBy {
            it.key
        }?.value?.map {
            it.first
        }?.sorted()?.first()
    }

    private fun Actor.moveTo(point: Point) {
        actors.remove(pos)
        grid[pos.y][pos.x] = '.'
        pos = point
        grid[pos.y][pos.x] = team.symbol
        actors[point] = this
    }

    private fun shortestPaths(src: Point, dst: Point): List<Path> {
        var minSteps = Int.MAX_VALUE
        val shortestPaths = hashSetOf<Path>()
        val q = PriorityQueue<Path>(compareBy({ it.mDist }, { it.first }))
        val seen = hashSetOf(src)

        src.openNeighbors().forEach { point ->
            q.add(Path(point, point, 1 + point.mDist(dst), 1))
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
                seen.add(path.current)
                if (path.steps < minSteps) {
                    path.current.openNeighbors().filter { it !in seen }.forEach { next ->
                        q.add(Path(path.first, next, path.steps + next.mDist(dst), path.steps + 1))
                    }
                }
            }
        }
        return shortestPaths.sortedBy { it.first }
    }

    fun run() {
        debug(grid)
        var round = 0

        outer@while (actors.isNotEmpty()) {
            //Thread.sleep(1000)
            // sort to find the turn order
            val sortedActors = actors.entries.sortedBy { it.key }.map { it.value }

            for (actor in sortedActors) {
                if (actor.hp <= 0) continue

                // look for actor in range to attack
                var enemy = actor.closestEnemy()
                if (enemy == null) {
                    // TODO: end game logic
                    val enemyPoints = actors.values.filter { it.team != actor.team }.map { it.pos }
                    if (enemyPoints.isEmpty()) {
                        break@outer
                    }
                    val dst = actor.getDestination(enemyPoints) ?: continue
                    actor.moveTo(dst)
                }

                // attack
                enemy = actor.closestEnemy()
                if (enemy != null) {
                    enemy.hp -= actor.ap
                    if (enemy.hp <= 0) {
                        actors.remove(enemy.pos)
                        grid[enemy.pos.y][enemy.pos.x] = '.'
                    }
                }

            }
            round += 1
            println(round)
            //debug(grid)
        }
        val remainingHp = actors.values.map{ it.hp }.sum()
        println("rounds: $round")
        println("total hp: $remainingHp")
        println("final result: ${remainingHp * round}")
    }
}

// 222372 is wrong (too high)
private fun debug(grid: List<CharArray>) {
    for (y in 0 until grid.size) {
        for (x in 0 until grid[y].size) {
            print(grid[y][x])
        }
        println()
    }
}

fun main() {
    val actors = HashMap<Point, Actor>()

    val grid = input("inputs/2018/15.txt").use {
        val lines = it.toList().map { it.toCharArray() }
        for (y in 0 until lines.size) {
            for (x in 0 until lines[y].size) {
                val pos = Point(x, y)
                when(lines[y][x]) {
                    'E' -> actors[pos] = Actor(Team.Elf, pos)
                    'G' -> actors[pos] = Actor(Team.Goblin, pos)
                }
            }
        }
        lines
    }

    Game(grid, actors).run()
}