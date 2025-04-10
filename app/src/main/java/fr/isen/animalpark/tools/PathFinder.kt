package fr.isen.animalpark.tools

import fr.isen.animalpark.models.Enclosure

fun findPath(
    enclosures: List<Enclosure>,
    startId: String,
    endId: String
): List<String>? {
    val visited = mutableSetOf<String>()
    val queue = ArrayDeque<List<String>>()
    queue.add(listOf(startId))

    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()
        val current = path.last()

        if (current == endId) return path

        if (!visited.contains(current)) {
            visited.add(current)
            val currentEnclosure = enclosures.find { it.id == current } ?: continue
            for (neighbor in currentEnclosure.neighbors) {
                if (!visited.contains(neighbor.id)) {
                    queue.add(path + neighbor.id)
                }
            }
        }
    }

    return null
}
