package fr.isen.animalpark.models

data class Biome (
    val id: String,
    val name: String,
    var color: String,
    val enclosures: List<Enclosure> = listOf()
)