package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class CreateRecetaUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(receta: Receta): Receta {
        if (recetaRepository.postReceta(receta)) {
            return receta
        } else {
            throw Exception("Failed to create receta")
        }
    }
}