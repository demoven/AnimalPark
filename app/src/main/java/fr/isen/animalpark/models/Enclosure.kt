package fr.isen.animalpark.models

data class Enclosure(
    val id: String,
    val id_biomes: String,
    var meal: String,
    val animals: List<Animal> = emptyList(),
    var isOpen: Boolean = false
)
