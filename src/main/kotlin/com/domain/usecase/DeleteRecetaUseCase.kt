package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class DeleteRecetaUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(receta: Receta): Receta {
        val id = receta.id ?: throw IllegalArgumentException("El ID de la receta no puede ser nulo")

        if (recetaRepository.deleteReceta(id)) {
            return receta
        } else {
            throw Exception("Failed to delete receta")
        }
    }
}