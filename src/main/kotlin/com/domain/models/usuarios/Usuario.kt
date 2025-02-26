package com.domain.models.usuarios

import kotlinx.serialization.Serializable

@Serializable
data class Usuario (
    val email: String,
    val password: String
)