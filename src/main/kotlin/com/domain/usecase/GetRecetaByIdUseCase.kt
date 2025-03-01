package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface


class GetRecetaByIdUseCase(private val recetaInterface: RecetaInterface) {
    suspend operator fun invoke(id: Int): Receta? {
        return recetaInterface.getRecetaById(id).firstOrNull()
    }
}