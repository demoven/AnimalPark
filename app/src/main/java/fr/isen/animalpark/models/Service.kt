package fr.isen.animalpark.models

import java.io.Serializable

data class Service(
    val id: String,
    val name: String,
    val availability: Boolean = true,
): Serializable
