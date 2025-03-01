package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class GetRecetaByNameUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(name: String): List<Receta> {
        return recetaRepository.getRecetaByName(name)
    }
}