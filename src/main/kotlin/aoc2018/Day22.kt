package aoc2018

import util.Point

private object Day22 {

    private const val DEPTH = 11541

    private class Cave(
            val targetPoint: Point,
            val depth: Int = DEPTH
    ) {
        val startPoint = Point(0, 0)
        val erosionLevels = HashMap<Point, Int>()
        val geoIndex = HashMap<Point, Int>()

        init {
            geoIndex[startPoint] = 0
            geoIndex[targetPoint] = 0
            getErosionLevel(targetPoint)
        }

        fun getGeoIndex(point: Point): Int = geoIndex.getOrPut(point) {
            when {
                point.y == 0 -> point.x * 16807
                point.x == 0 -> point.y * 48271
                else -> getErosionLevel(point.copy(x = point.x - 1)) *
                        getErosionLevel(point.copy(y = point.y - 1))
            }
        }

        fun getErosionLevel(point: Point): Int = erosionLevels.getOrPut(point) {
            (getGeoIndex(point) + depth) % 20183
        }

        fun riskLevel() = (startPoint.x .. targetPoint.x).flatMap { x ->
            (startPoint.y .. targetPoint.y).map { y ->
                terrainType(Point(x, y))
            }
        }.sum()

        private fun terrainType(point: Point): Int {
            return getErosionLevel(point) % 3
        }

        fun print() {
            for (y in startPoint.y .. targetPoint.y) {
                for (x in startPoint.x .. targetPoint.x) {
                    print(when (terrainType(Point(x, y))) {
                        0 -> "."
                        1 -> "="
                        2 -> "|"
                        else -> "?"
                    })
                }
                println()
            }
        }
    }

    fun run() {
        val cave = Cave(Point(10, 10), 510)
//        val cave = Cave(Point(14, 778))
        cave.print()
        println(cave.riskLevel())
    }
}

fun main() {
    Day22.run()
}
