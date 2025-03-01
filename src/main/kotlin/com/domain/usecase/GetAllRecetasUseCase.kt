package com.domain.usecase

import com.domain.models.recetas.Receta
import com.domain.repository.RecetaInterface

class GetAllRecetasUseCase(private val recetaRepository: RecetaInterface) {
    suspend operator fun invoke(): List<Receta> {
        return recetaRepository.getAllRecetas()
    }
}