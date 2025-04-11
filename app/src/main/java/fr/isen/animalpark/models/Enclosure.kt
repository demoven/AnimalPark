package fr.isen.animalpark.models

import java.io.Serializable

data class Neighbor(
    val id: String = ""
) : Serializable

data class Enclosure(
    val id: String,
    val id_biomes: String,
    var meal: String,
    val animals: List<Animal> = emptyList(),
    var isOpen: Boolean = false,
    val neighbors: List<Neighbor> = emptyList(),
    val reviews: Map<String, Review>? = null
) : Serializable
