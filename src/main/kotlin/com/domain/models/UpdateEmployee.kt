package com.domain.models

import kotlinx.serialization.Serializable

/*
Sólo para serializar en consultas
patch
 */
@Serializable
data class UpdateEmployee (
    val dni: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val salary: Salary? = null
)