package aoc2019

import util.lcm
import kotlin.math.abs

private object Day12 {

    class Moon(
            val pos: IntArray,
            val vel: IntArray = IntArray(3)
    ) {

        constructor(vararg pos: Int): this(pos)

        fun updateAxis(axis: Int, moons: List<Moon>) {
            for (moon in moons) {
                vel[axis] += moon.pos[axis].compareTo(pos[axis])
            }
            pos[axis] += vel[axis]
        }

        fun updateAllAxis(moons: List<Moon>) {
            for (moon in moons) {
                vel[0] += moon.pos[0].compareTo(pos[0])
                vel[1] += moon.pos[1].compareTo(pos[1])
                vel[2] += moon.pos[2].compareTo(pos[2])
            }
            pos[0] += vel[0]
            pos[1] += vel[1]
            pos[2] += vel[2]
        }

        fun totalEnergy() = pos.map { abs(it) }.sum() * vel.map { abs(it) }.sum()

        fun copy() = Moon(pos.copyOf(), vel.copyOf())
    }

    fun totalEnergyAfterSteps(steps: Int) {
        val moons = parseInput()
        repeat(steps) {
            val copy = moons.map { it.copy() }
            moons.forEach { it.updateAllAxis(copy) }
        }
        println("total energy: " + moons.map { it.totalEnergy() }.sum())
    }

    private fun findAxisPeriod(axis: Int, moons: List<Moon>): Long {
        val initialState = moons.map { it.pos[axis] }
        val current = moons.map { it.copy() }
        var steps = 0L

        do {
            val copy = current.map { it.copy() }
            current.forEach { it.updateAxis(axis, copy) }
            steps++
        } while (current.any { it.vel[axis] != 0 } || current.map { it.pos[axis] } != initialState)
        return steps
    }

    fun countStepsUntilRepeatState() {
        val moons = parseInput()
        val copy = moons.map { it.copy() }
        val periodX = findAxisPeriod(0, copy)
        val periodY = findAxisPeriod(1, copy)
        val periodZ = findAxisPeriod(2, copy)
        val totalPeriod = lcm(periodX, periodY, periodZ)
        println("found repeat state after $totalPeriod steps")
    }

    private fun parseInput() = listOf(
        Moon(14, 15, -2),
        Moon(17, -3, 4),
        Moon(6, 12, -13),
        Moon(-2, 10, -8)
    )
}

fun main() {
    Day12.totalEnergyAfterSteps(1000)
    Day12.countStepsUntilRepeatState()
}