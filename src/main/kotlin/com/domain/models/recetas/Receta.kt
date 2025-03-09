package com.domain.models.recetas

import kotlinx.serialization.Serializable

@Serializable
data class Receta(
    val id: Int? = null,
    val userId: Int = 0,
    val name: String,
    val description: String,
    val ingredients: String,
    val calories: String,
    val imageUrl: String? = null
)