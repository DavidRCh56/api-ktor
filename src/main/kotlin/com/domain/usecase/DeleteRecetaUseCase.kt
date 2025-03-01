package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class DeleteRecetaUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(receta: Receta): Receta {
        if (recetaRepository.deleteReceta(receta.id)) {
            return receta
        } else {
            throw Exception("Failed to delete receta")
        }
    }
}