package com.data.repository.recetas

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.repository.RecetaInterface


class DatabaseRecetaRepository : RecetaInterface {
    override fun getAllRecetas(): List<Receta> {
        TODO("Not yet implemented")
    }

    override fun getRecetaById(id: Int): List<Receta> {
        TODO("Not yet implemented")
    }

    override fun getRecetaByUserId(userId: String): Receta? {
        TODO("Not yet implemented")
    }

    override fun getRecetaByName(name: String): List<Receta> {
        TODO("Not yet implemented")
    }

    override fun postReceta(receta: Receta): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateReceta(receta: UpdateReceta, id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteReceta(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}