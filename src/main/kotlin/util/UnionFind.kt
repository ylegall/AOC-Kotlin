package util


class UnionFind(initialSize: Int) {

    private var sizes = Array(initialSize) { 1 }
    private val groups = Array(initialSize) { it }

    var size = initialSize
        private set

    fun size(item: Int) = sizes[find(item)]

    fun groupSizes(): Map<Int, Int> {
        return groups.associate {
            val root = find(it)
            root to sizes[root]
        }
    }

    fun find(item: Int): Int {
        var root = item
        while (root != groups[root]) {
            root = groups[root]
        }
        var node = item
        while (node != root) {
            val next = groups[node]
            groups[node] = root
            node = next
        }
        return root
    }

    fun union(item1: Int, item2: Int) {
        val root1 = find(item1)
        val root2 = find(item2)

        if (root1 == root2) return

        if (sizes[root1] > sizes[root2]) {
            groups[root1] = root2
            sizes[root2] += sizes[root1]
        } else {
            groups[root2] = root1
            sizes[root1] += sizes[root2]
        }
        size--
    }
}