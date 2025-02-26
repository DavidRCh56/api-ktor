package com.data.repository.recetas

import com.data.models.RecetaData
import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.repository.RecetaInterface

class MemoryRecetaRepository : RecetaInterface {
    override fun getAllRecetas(): List<Receta> {
        return RecetaData.listaReceta
    }

    override fun getRecetaById(id: Int): List<Receta> {
        return RecetaData.listaReceta.filter { it.id == id }
    }

    override fun getRecetaByName(name: String): List<Receta> {
        return RecetaData.listaReceta.filter { it.name.contains(name, ignoreCase = true) }
    }

    override fun getRecetaByUserId(userId: String): Receta? {
        return RecetaData.listaReceta.find { it.userId == userId }
    }

    override fun postReceta(receta: Receta): Boolean {
        return if (RecetaData.listaReceta.any { it.id == receta.id }) {
            false
        } else {
            RecetaData.listaReceta.add(receta)
            true
        }
    }

    override fun updateReceta(receta: UpdateReceta, id: Int): Boolean {
        val index = RecetaData.listaReceta.indexOfFirst { it.id == id }
        if (index == -1) return false

        val recetaExistente = RecetaData.listaReceta[index]
        val recetaActualizada = recetaExistente.copy(
            name = receta.name ?: recetaExistente.name,
            description = receta.description ?: recetaExistente.description,
            ingredients = receta.ingredients ?: recetaExistente.ingredients,
            calories = receta.calories ?: recetaExistente.calories,
            imageUrl = receta.imageUrl ?: recetaExistente.imageUrl
        )

        RecetaData.listaReceta[index] = recetaActualizada
        return true
    }

    override fun deleteReceta(id: Int): Boolean {
        return RecetaData.listaReceta.removeIf { it.id == id }
    }
}