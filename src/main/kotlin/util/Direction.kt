package util

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun reverse() = when(this) {
        NORTH -> SOUTH
        EAST  -> WEST
        SOUTH -> NORTH
        WEST  -> EAST
    }

    fun turnRight() = values().let { it[(ordinal + 1) % it.size] }

    fun turnLeft() = values().let { it[(it.size + ordinal - 1) % it.size] }
}

fun Point.move(direction: Direction) = when(direction) {
    Direction.NORTH -> Point(x, y - 1)
    Direction.EAST  -> Point(x + 1, y)
    Direction.SOUTH -> Point(x, y + 1)
    Direction.WEST  -> Point(x - 1, y)
}