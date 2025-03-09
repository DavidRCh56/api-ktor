package com.domain.repository

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta

interface RecetaInterface {
    fun getAllRecetas(): List<Receta>
    fun getRecetaById(id: Int): Receta?
    fun getRecetaByName(name: String): List<Receta>
    fun getRecetaByUserId(userId: Int): List<Receta>
    fun postReceta(receta: Receta): Boolean
    fun updateReceta(receta: UpdateReceta, id: Int, userId: Int): Boolean
    fun deleteReceta(id: Int): Boolean
}
