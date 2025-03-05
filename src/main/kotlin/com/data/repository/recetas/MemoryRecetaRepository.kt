package com.data.repository.recetas

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.repository.RecetaInterface
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

// Usamos IntIdTable para que el campo id se genere automáticamente y sea la clave primaria.
object RecetasTable : IntIdTable("recetas") {
    val userId = varchar("userId", 50)
    val name = varchar("name", 500)
    val description = varchar("descripcion", 500)
    val ingredients = varchar("ingredientes", 500)
    val calories = varchar("calories", 500)
    // Si se requiere imageUrl en la tabla, se puede agregar, por ejemplo:
    // val imageUrl = varchar("imageUrl", 500).nullable()
}

class MemoryRecetaRepository : RecetaInterface {

    override fun getAllRecetas(): List<Receta> = transaction {
        RecetasTable.selectAll().map {
            Receta(
                id = it[RecetasTable.id].value,
                userId = it[RecetasTable.userId],
                name = it[RecetasTable.name],
                description = it[RecetasTable.description],
                ingredients = it[RecetasTable.ingredients],
                calories = it[RecetasTable.calories],
                imageUrl = null // O it[RecetasTable.imageUrl] si se agregó la columna
            )
        }
    }

    override fun getRecetaById(id: Int): List<Receta> = transaction {
        RecetasTable.selectAll().where { RecetasTable.id eq id }.map {
            Receta(
                id = it[RecetasTable.id].value,
                userId = it[RecetasTable.userId],
                name = it[RecetasTable.name],
                description = it[RecetasTable.description],
                ingredients = it[RecetasTable.ingredients],
                calories = it[RecetasTable.calories],
                imageUrl = null
            )
        }
    }

    override fun getRecetaByName(name: String): List<Receta> = transaction {
        RecetasTable.selectAll().where { RecetasTable.name like "%$name%" }.map {
            Receta(
                id = it[RecetasTable.id].value,
                userId = it[RecetasTable.userId],
                name = it[RecetasTable.name],
                description = it[RecetasTable.description],
                ingredients = it[RecetasTable.ingredients],
                calories = it[RecetasTable.calories],
                imageUrl = null
            )
        }
    }

    override fun getRecetaByUserId(userId: String): Receta? = transaction {
        RecetasTable.selectAll().where { RecetasTable.userId eq userId }
            .map {
                Receta(
                    id = it[RecetasTable.id].value,
                    userId = it[RecetasTable.userId],
                    name = it[RecetasTable.name],
                    description = it[RecetasTable.description],
                    ingredients = it[RecetasTable.ingredients],
                    calories = it[RecetasTable.calories],
                    imageUrl = null
                )
            }
            .firstOrNull()
    }

    // Inserción de receta: se genera automáticamente el id con insertAndGetId.
    override fun postReceta(receta: Receta): Boolean = transaction {
        val generatedId = RecetasTable.insertAndGetId { row ->
            row[userId] = receta.userId
            row[name] = receta.name
            row[description] = receta.description
            row[ingredients] = receta.ingredients
            row[calories] = receta.calories
            // Si se agrega imageUrl a la tabla, se puede asignar aquí:
            // row[imageUrl] = receta.imageUrl
        }
        generatedId.value > 0
    }

    override fun updateReceta(receta: UpdateReceta, id: Int): Boolean = transaction {
        val updateCount = RecetasTable.update({ RecetasTable.id eq id }) {
            receta.name?.let { newName -> it[name] = newName }
            receta.description?.let { newDesc -> it[description] = newDesc }
            receta.ingredients?.let { newIngredients -> it[ingredients] = newIngredients }
            receta.calories?.let { newCalories -> it[calories] = newCalories }
            // Para actualizar imageUrl, se debe agregar la columna en la tabla y actualizarla aquí.
        }
        updateCount > 0
    }

    override fun deleteReceta(id: Int): Boolean = transaction {
        RecetasTable.deleteWhere { RecetasTable.id eq id } > 0
    }
}
