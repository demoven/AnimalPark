package fr.isen.animalpark.tools

import android.content.Context
import fr.isen.animalpark.R
import fr.isen.animalpark.models.Enclosure



fun findPath(
    enclosures: List<Enclosure>?,
    startId: Enclosure?,
    endId: Enclosure?,
    context: Context
): List<String> {
    if (startId == null || endId == null) {
        val msg = context.getString(R.string.error_start)
        return listOf(msg)
    }

    if (enclosures.isNullOrEmpty()) {
        val msg = context.getString(R.string.error_list_enclosures)
        return listOf(msg)
    }

    if (startId.animals.isNullOrEmpty()) {
        val msg = context.getString(R.string.error_no_animals)
        return listOf(msg)
    }

    val visited = mutableSetOf<String>()
    val queue = ArrayDeque<List<String>>()
    queue.add(listOf(startId.animals.firstOrNull()?.name ?: ""))

    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()
        val currentAnimalName = path.lastOrNull() ?: continue

        val currentEnclosure = enclosures.find { enclosure ->
            enclosure.animals?.any { it.name == currentAnimalName } == true
        }

        if (currentEnclosure?.id == endId.id) {
            return path
        }

        if (currentEnclosure != null && !visited.contains(currentEnclosure.id)) {
            visited.add(currentEnclosure.id)

            currentEnclosure.neighbors?.let { neighbors ->
                for (neighbor in neighbors) {
                    val neighborEnclosure = enclosures.find { it.id == neighbor.id }
                    if (neighborEnclosure != null && !visited.contains(neighbor.id)) {
                        val neighborAnimalName = neighborEnclosure.animals?.firstOrNull()?.name
                        if (neighborAnimalName != null) {
                            queue.add(path + neighborAnimalName)
                        }
                    }
                }
            }
        }
    }

    val msg = context.getString(R.string.error_path_not_found)
    return listOf(msg)
}
