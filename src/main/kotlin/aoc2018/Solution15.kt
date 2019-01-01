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
        val last: Point,
        val steps: Int
)

class Game(
        private val grid: List<CharArray>,
        private val actors: MutableMap<Point, Actor>
) {
    private fun Point.value() = grid[y][x]

    private fun Point.neighbors(): List<Point> {
        return listOf(
                Point(x, y - 1),
                Point(x - 1, y),
                Point(x + 1, y),
                Point(x, y + 1)
        ).filter { it.value() != '#' }
    }

    private fun Point.openNeighbors() = neighbors().filter { it.value() == '.' }

    private fun Actor.closestEnemy() = pos.neighbors().mapNotNull {
        actors[it]
    }.filter {
        it.team != team && it.hp > 0
    }.sortedWith(compareBy(
            { it.hp }, { it.pos })
    ).firstOrNull()

    private fun Actor.enemyPoints() = actors.values.filter {
        it.hp > 0 && it.team != team
    }.map {
        it.pos
    }

    private fun Actor.getDestination(enemyPoints: List<Point>): Point? {
        return enemyPoints.flatMap {
            it.openNeighbors()
        }.distinct().let { targets ->
            shortestPaths(pos, targets.toSet())
        }?.sortedWith(compareBy(
                { it.last }, { it.first }
        ))?.first()?.first
    }

    private fun Actor.moveTo(point: Point) {
        actors.remove(pos)
        grid[pos.y][pos.x] = '.'
        pos = point
        grid[point.y][point.x] = team.symbol
        actors[point] = this
    }

    private fun shortestPaths(src: Point, targets: Set<Point>): List<Path>? {
        var minSteps = Int.MAX_VALUE
        val shortestPaths = arrayListOf<Path>()
        val q = ArrayDeque<Path>()
        val seen = hashSetOf(src)

        src.openNeighbors().forEach { point ->
            q.add(Path(point, point, 1))
        }

        while (q.isNotEmpty()) {
            val path = q.poll()
            if (path.last in targets) {
                shortestPaths.add(path)
                if (path.steps < minSteps) {
                    minSteps = path.steps
                }
            } else {
                if (path.last !in seen && path.steps < minSteps) {
                    val nextPoints = path.last.openNeighbors().filter { it !in seen }
                    for (next in nextPoints) {
                        q.add(Path(path.first, next, path.steps + 1))
                    }
                }
                seen.add(path.last)
            }
        }
        return shortestPaths.groupBy { it.steps }.minBy { it.key }?.value
    }

    fun run(elfAttackPower: Int = 3, allowElfDeaths: Boolean = true): Team {
        //debug(grid)
        println("attack power: $elfAttackPower")
        var round = 0

        outer@while (actors.isNotEmpty()) {
            // sort to find the turn order
            val sortedActors = actors.entries.sortedBy { it.key }.map { it.value }

            for (actor in sortedActors) {
                if (actor.hp <= 0) continue

                // look for actor in range to attack
                var enemy = actor.closestEnemy()
                if (enemy == null) {
                    val enemyPoints = actor.enemyPoints()
                    if (enemyPoints.isEmpty()) {
                        break@outer
                    }
                    val dst = actor.getDestination(enemyPoints) ?: continue
                    actor.moveTo(dst)
                }

                // attack
                enemy = actor.closestEnemy()
                if (enemy != null) {
                    enemy.hp -= if (actor.team == Team.Elf) {
                        elfAttackPower
                    } else {
                        actor.ap
                    }

                    if (enemy.hp <= 0) {
                        if (enemy.team == Team.Elf && !allowElfDeaths) {
                            println("AN ELF HAS DIED")
                            return Team.Goblin
                        }
                        actors.remove(enemy.pos)
                        grid[enemy.pos.y][enemy.pos.x] = '.'
                    }
                }
            }
            round += 1
            //println(round)
            //debug(grid)
        }
        val survivors = actors.values.filter{ it.hp > 0 }
        val remainingHp = survivors.map{ it.hp }.sum()
        println("\nrounds: $round")
        println("elf attack power: $elfAttackPower")
        println("total hp: $remainingHp")
        println("final result: ${remainingHp * round}")
        return survivors.first().team
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

private fun copyOf(grid: List<CharArray>) = grid.map { row -> Arrays.copyOf(row, row.size) }.toList()
private fun copyOf(actors: HashMap<Point, Actor>) = actors.mapValues { it.value.copy() }.toMutableMap()

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

    // part 1:
    Game(copyOf(grid), copyOf(actors)).run()

    // part 2:
    (4 .. 200).asSequence().map { elfAttackPower ->
        Pair(elfAttackPower, Game(copyOf(grid), copyOf(actors)).run(elfAttackPower, allowElfDeaths = false))
    }.first {
        it.second == Team.Elf
    }.let {
        println("required elf attack power: ${it.first}")
    }
}