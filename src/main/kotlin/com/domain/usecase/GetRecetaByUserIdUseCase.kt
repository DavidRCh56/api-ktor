package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class GetRecetaByUserIdUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(userId: String): Receta? {
        return recetaRepository.getRecetaByUserId(userId)
    }
}