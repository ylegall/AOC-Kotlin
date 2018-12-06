import java.util.stream.Stream

private fun countOverlappingRectangles(stream: Stream<String>): Int {
    val pattern = Regex("""@ (\d+),(\d+): (\d+)x(\d+)""")
    val points = hashMapOf<Pair<Int,Int>, Int>()
    for (line in stream) {
        val match = pattern.find(line) ?: throw Exception("error parsing line: $line")
        val x = match.groups[1]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val y = match.groups[2]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val w = match.groups[3]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val h = match.groups[4]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        for (i in x until x + w) {
            for (j in y until y + h) {
                val point = Pair(i,j)
                points[point] = points.getOrDefault(point, 0) + 1
            }
        }
    }
    return points.values.filter { it >= 2 }.size
}

private fun findNonOverlappingRectangle(stream: Stream<String>): Int {
    var maxId = 0
    val overlaps = hashSetOf<Int>()
    val pattern = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")
    val points = hashMapOf<Pair<Int,Int>, Int>()
    for (line in stream) {
        val match = pattern.find(line) ?: throw Exception("error parsing line: $line")
        val id = match.groups[1]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val x = match.groups[2]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val y = match.groups[3]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val w = match.groups[4]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        val h = match.groups[5]?.value?.toInt() ?: throw Exception("error parsing line: $line")
        for (i in x until x + w) {
            for (j in y until y + h) {
                val point = Pair(i,j)
                val lastId = points.getOrDefault(point, id)
                if (lastId != id) {
                    overlaps.add(id)
                    overlaps.add(lastId)
                }
                points[point] = id
            }
        }
        if (id >= maxId) maxId = id
    }
    return (1..maxId).first { it !in overlaps}
}

fun main() {
    input("inputs/input-3.txt").use { stream ->
        println(countOverlappingRectangles(stream))
    }
    input("inputs/input-3.txt").use { stream ->
        println(findNonOverlappingRectangle(stream))
    }
}