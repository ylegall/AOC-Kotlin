package aoc2020

import aoc2020.Day17.neighbors
import java.io.File


private object Day17 {

    data class Coord(val x: Int, val y: Int, val z: Int)

    class Grid(
            val cubes: Map<Coord, Boolean>,
            val minCorner: Coord,
            val maxCorner: Coord,
    ) {

        fun print() {
            println("$minCorner - $maxCorner")
            (minCorner.z .. maxCorner.z).forEach { z ->
                println("level: $z")
                (minCorner.y .. maxCorner.y).forEach { y ->
                    (minCorner.x .. maxCorner.x).forEach { x ->
                        print(if (cubes.getOrDefault(Coord(x, y, z), false)) '#' else '.')
                    }
                    println()
                }
                println()
            }
        }

        fun nextCubeState(coord: Coord): Boolean {
            val active = cubes[coord] ?: false
            val activeNeighbors = coord.neighbors().map { neighbor -> cubes[neighbor] ?: false }.count { it }
            return when {
                active && activeNeighbors in 2 .. 3 -> true
                !active && activeNeighbors == 3     -> true
                else                                -> false
            }
        }

        fun next(): Grid {
            val nextGrid = (minCorner.z-1 .. maxCorner.z+1).flatMap { z ->
                (minCorner.y-1 .. maxCorner.y+1).flatMap { y ->
                    (minCorner.x-1 .. maxCorner.x+1).map { x ->
                        val nextCoord = Coord(x, y, z)
                        nextCoord to nextCubeState(nextCoord)
                    }
                }
            }.toMap()
            return Grid(
                    nextGrid,
                    Coord(minCorner.x - 1, minCorner.y - 1, minCorner.z - 1),
                    Coord(maxCorner.x + 1, maxCorner.y + 1, maxCorner.z + 1)
            )
        }

        fun count(): Int {
            return cubes.values.count { it }
        }
    }

    fun Coord.neighbors() = (-1 .. 1).flatMap { dx ->
        (-1 .. 1).flatMap { dy ->
            (-1 .. 1).map { dz ->
                this.copy(x = x + dx, y = y + dy, z = z + dz)
            }
        }
    }.filter { it != this }

}

private object Day172 {

    data class Coord(val x: Int, val y: Int, val z: Int, val w: Int)

    class Grid(
            val cubes: Map<Coord, Boolean>,
            val minCorner: Coord,
            val maxCorner: Coord,
    ) {

        fun print() {
            println("$minCorner - $maxCorner")
            (minCorner.w .. maxCorner.w).forEach { w ->
                (minCorner.z..maxCorner.z).forEach { z ->
                    println("level: $z, $w")
                    (minCorner.y..maxCorner.y).forEach { y ->
                        (minCorner.x..maxCorner.x).forEach { x ->
                            print(if (cubes.getOrDefault(Coord(x, y, z, w), false)) '#' else '.')
                        }
                        println()
                    }
                    println()
                }
                println()
            }
        }

        fun nextCubeState(coord: Coord): Boolean {
            val active = cubes[coord] ?: false
            val activeNeighbors = coord.neighbors().map { neighbor -> cubes[neighbor] ?: false }.count { it }
            return when {
                active && activeNeighbors in 2 .. 3 -> true
                !active && activeNeighbors == 3     -> true
                else                                -> false
            }
        }

        fun next(): Grid {
            val nextGrid =
            (minCorner.w-1 .. maxCorner.w+1).flatMap { w ->
                (minCorner.z - 1..maxCorner.z + 1).flatMap { z ->
                    (minCorner.y - 1..maxCorner.y + 1).flatMap { y ->
                        (minCorner.x - 1..maxCorner.x + 1).map { x ->
                            val nextCoord = Coord(x, y, z, w)
                            nextCoord to nextCubeState(nextCoord)
                        }
                    }
                }
            }.toMap()

            return Grid(
                    nextGrid,
                    Coord(minCorner.x - 1, minCorner.y - 1, minCorner.z - 1, minCorner.w - 1),
                    Coord(maxCorner.x + 1, maxCorner.y + 1, maxCorner.z + 1, maxCorner.w + 1)
            )
        }

        fun count(): Int {
            return cubes.values.count { it }
        }
    }

    fun Coord.neighbors() =
        (-1 .. 1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).flatMap { dz ->
                    (-1..1).map { dw ->
                        this.copy(x = x + dx, y = y + dy, z = z + dz, w = w + dw)
                    }
                }
            }
        }.filter { it != this }
}

fun main() {
    val input = File("inputs/2020/17.txt").readLines()

    val cubes1 = input.flatMapIndexed { y, row ->
        row.mapIndexed { x, char ->
            Day17.Coord(x, y, 0) to (char == '#')
        }
    }.toMap()
    val minCorner = Day17.Coord(0, 0, 0)
    val maxCorner = Day17.Coord(input[0].length - 1, input.size - 1, 0)

    var grid = Day17.Grid(cubes1, minCorner, maxCorner)
    //grid.print()
    (0 until 6).forEach {
        //println("Generation $it")
        grid = grid.next()
        //grid.print()
        println("Count: ${grid.count()}")
    }

    val cubes2 = input.flatMapIndexed { y, row ->
        row.mapIndexed { x, char ->
            Day172.Coord(x, y, 0, 0) to (char == '#')
        }
    }.toMap()
    val minCorner2 = Day172.Coord(0, 0, 0, 0)
    val maxCorner2 = Day172.Coord(input[0].length - 1, input.size - 1, 0, 0)

    var grid2 = Day172.Grid(cubes2, minCorner2, maxCorner2)
    //grid2.print()
    (0 until 6).forEach {
        //println("Generation $it")
        grid2 = grid2.next()
        //grid2.print()
        println("Count: ${grid2.count()}")
    }
}