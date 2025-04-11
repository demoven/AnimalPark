package fr.isen.animalpark.models

import java.io.Serializable

data class Review (
    val rating: Int=0,
    val comment: String=""

): Serializable