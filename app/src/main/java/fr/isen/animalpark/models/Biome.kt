package fr.isen.animalpark.models

data class Biome(
    val id: String,
    val name: String,
    val color: String,
    val enclosures: List<Enclosure> = emptyList(),
    val services: List<Service> = emptyList()
)

