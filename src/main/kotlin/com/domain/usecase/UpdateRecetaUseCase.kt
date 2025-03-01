package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.repository.RecetaInterface


class UpdateRecetaUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(id: Int, receta: Receta): Boolean {
        return recetaRepository.updateReceta(
            UpdateReceta(
                id = receta.id,
                userId = receta.userId,
                name = receta.name,
                description = receta.description,
                ingredients = receta.ingredients,
                calories = receta.calories,
                imageUrl = receta.imageUrl
            ),
            id
        )
    }
}