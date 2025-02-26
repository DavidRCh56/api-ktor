package com.domain.models.recetas

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReceta (
    val id: Int? = null,
    val userId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val ingredients: String? = null,
    val calories: String? = null,
    val imageUrl: String? = null
)