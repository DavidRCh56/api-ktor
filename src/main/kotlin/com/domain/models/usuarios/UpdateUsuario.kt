package com.domain.models.usuarios

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsuario (
    val email: String? = null,
    val password: String? = null
)