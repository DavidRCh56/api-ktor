package com.domain.repository

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta

interface RecetaInterface {
    fun getAllRecetas () : List <Receta>

    fun getRecetaById ( id: Int) : List<Receta>

    fun getRecetaByName ( name : String) : List<Receta>

    fun getRecetaByUserId (userId: String) : Receta?

    fun postReceta(receta: Receta) : Boolean

    fun updateReceta(receta: UpdateReceta, id: Int) : Boolean

    fun deleteReceta(id : Int) : Boolean
}