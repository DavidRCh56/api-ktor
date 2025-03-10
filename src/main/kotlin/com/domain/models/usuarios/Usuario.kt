package com.domain.models.usuarios

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val email: String,
    val password: String,
    val token: String? = null
)
