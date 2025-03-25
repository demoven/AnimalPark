package fr.isen.animalpark.models

import java.io.Serializable

data class Animal (
    val id: String,
    val name: String,
    val id_animal: String,
    val id_enclos: String,
) : Serializable