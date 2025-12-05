package util


fun <T> topologicalSort(dependencies: Map<T, Iterable<T>>): List<T> {
    var remainingItems = dependencies.mapValues { it.value.toSet() }
    val sortedItems = LinkedHashSet<T>(dependencies.keys.size)
    while (remainingItems.isNotEmpty()) {
        val (itemsWithoutDependencies, itemsWithDependencies) = remainingItems.entries.partition { it.value.isEmpty() }
        if (itemsWithDependencies.isEmpty()) throw Exception("no next available step")
        sortedItems.addAll(itemsWithoutDependencies.map { it.key })

        remainingItems = itemsWithDependencies.associate { (item, dependencies) ->
            item to dependencies.filter { it !in sortedItems }.toSet()
        }
    }
    return sortedItems.toList()
}

fun <T> stratifiedTopologicalSort(dependencies: Map<T, Iterable<T>>): List<Set<T>> {
    var remainingItems = dependencies.mapValues { it.value.toSet() }
    val sortedItems = LinkedHashSet<T>()
    val sortedGroups = ArrayList<Set<T>>()
    while (remainingItems.isNotEmpty()) {
        val (itemsWithoutDependencies, itemsWithDependencies) = remainingItems.entries.partition { it.value.isEmpty() }
        val nextItems = itemsWithoutDependencies.map { it.key }
        sortedGroups.add(nextItems.toSet())
        sortedItems.addAll(nextItems)
        remainingItems = itemsWithDependencies.associate { (item, dependencies) ->
            item to dependencies.filter { it !in sortedItems }.toSet()
        }
    }
    return sortedGroups
}
