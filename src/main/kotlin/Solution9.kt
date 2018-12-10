
const val PLAYERS = 425

class Ring {
    private class Node(val marble: Int) {
        var prev = this
        var next = this

        fun remove() {
            prev.next = this.next
            next.prev = this.prev
            next = this
            prev = this
        }

        fun insertAfter(node: Node) {
            node.prev = this
            node.next = next
            next.prev = node
            next = node
        }
    }

    private var current = Node(0)
    private val start = current

    fun move(amount: Int) {
        if (amount > 0) {
            for (i in 0 until amount) current = current.next
        } else {
            for (i in 0 until -amount) current = current.prev
        }
    }

    fun insert(value: Int) {
        current.insertAfter(Node(value))
        current = current.next
    }

    fun remove(): Int {
        val result = current
        current = current.next
        return result.apply { remove() }.marble
    }

    fun debug() {
        var cursor = start
        do {
            if (cursor == current) {
                print("(${cursor.marble}) ")
            } else {
                print("${cursor.marble} ")
            }
            cursor = cursor.next
        } while (cursor != start)
        println()
    }
}

private fun winningScore(numMarbles: Int): Long {
    var player = 1
    val ring = Ring()
    val scores = hashMapOf<Int, Long>()

    for (marble in 1 .. numMarbles) {
        if (marble % 23 == 0) {
            ring.move(-7)
            scores[player] = scores.getOrDefault(player, 0) + marble
            scores[player] = scores[player]!! + ring.remove()
        } else {
            ring.move(1)
            ring.insert(marble)
        }

        // print("[$player] ")
        // ring.debug()

        player = (player + 1) % PLAYERS
    }
    return scores.values.max()!!
}

fun main() {
    println(winningScore(70848))
    println(winningScore(7084800))
}