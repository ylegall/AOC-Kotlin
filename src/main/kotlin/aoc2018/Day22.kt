package aoc2018

private object Day22 {

    private const val DEPTH = 11541

    private class Cave(
            targetY: Int,
            targetX: Int,
            val depth: Int = DEPTH
    ) {
        val rows = targetY + 1
        val cols = targetX + 1
        val erosionLevel = Array(rows) { arrayOfNulls<Int>(cols) }
        val geoIndex = Array(rows) { arrayOfNulls<Int>(cols) }

        init {
            geoIndex[0][0] = 0
            for (row in 0 until rows) {
                geoIndex[row][0] = 48271 * row
            }
            for (col in 0 until cols) {
                geoIndex[0][col] = 16807 * col
            }
            getErosionLevel(0, 0)
            getErosionLevel(rows - 1, cols - 1)
            geoIndex[rows - 1][cols - 1] = 0
        }

        fun getGeoIndex(row: Int, col: Int): Int {
            if (geoIndex[row][col] == null) {
                geoIndex[row][col] = getErosionLevel(row, col - 1) * getErosionLevel(row - 1, col)
            }
            return geoIndex[row][col]!!
        }

        fun getErosionLevel(row: Int, col: Int): Int {
            if (erosionLevel[row][col] == null) {
                erosionLevel[row][col] = (getGeoIndex(row, col) + depth) % 20183
            }
            return erosionLevel[row][col]!!
        }

        fun riskLevel() = erosionLevel.sumBy { it.sumBy { it!! % 3 } }

        fun print() {
            for (row in erosionLevel.indices) {
                println(erosionLevel[row].joinToString("") {
                    when (it!! % 3) {
                        0 -> "."
                        1 -> "="
                        2 -> "|"
                        else -> throw Exception("")
                    }
                })
            }
        }
    }

    fun run() {
//        val cave = Cave(10, 10, 510)
        val cave = Cave(778, 14)
//        cave.print()
        println(cave.riskLevel())
    }
}

fun main() {
    Day22.run()
}
