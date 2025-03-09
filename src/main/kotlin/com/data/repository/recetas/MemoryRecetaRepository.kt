package com.data.repository.recetas

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.repository.RecetaInterface
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object RecetasTable : IntIdTable("recetas") {
    val userId = integer("userId")
    val name = varchar("name", 500)
    val description = varchar("descripcion", 500)
    val ingredients = varchar("ingredientes", 500)
    val calories = varchar("calories", 500)
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
                imageUrl = null
            )
        }
    }

    override fun getRecetaById(id: Int): Receta? = transaction {
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
        }.firstOrNull()
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

    override fun getRecetaByUserId(userId: Int): List<Receta> = transaction {
        RecetasTable.selectAll().where { RecetasTable.userId eq userId }.map {
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

    override fun postReceta(receta: Receta): Boolean = transaction {
        val generatedId = RecetasTable.insertAndGetId { row ->
            row[userId] = receta.userId
            row[name] = receta.name
            row[description] = receta.description
            row[ingredients] = receta.ingredients
            row[calories] = receta.calories
        }
        generatedId.value > 0
    }

    override fun updateReceta(receta: UpdateReceta, id: Int, userId: Int): Boolean = transaction {
        val updateCount = RecetasTable.update({ (RecetasTable.id eq id) and (RecetasTable.userId eq userId) }) {
            receta.name?.let { newName -> it[name] = newName }
            receta.description?.let { newDesc -> it[description] = newDesc }
            receta.ingredients?.let { newIngredients -> it[ingredients] = newIngredients }
            receta.calories?.let { newCalories -> it[calories] = newCalories }
        }
        updateCount > 0
    }

    override fun deleteReceta(id: Int): Boolean = transaction {
        RecetasTable.deleteWhere { RecetasTable.id eq id } > 0
    }
}
