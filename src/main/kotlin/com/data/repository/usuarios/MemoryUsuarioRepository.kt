package com.data.repository.usuarios

import com.domain.models.usuarios.Usuario
import com.domain.models.usuarios.UpdateUsuario
import com.domain.repository.UsuarioInterface
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

// Define la tabla de usuarios en este mismo archivo
object UsuariosTable : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 50).uniqueIndex()
    val password = varchar("password", 64)
    val token = varchar("token", 512).nullable() // Se define como nullable
    override val primaryKey = PrimaryKey(id)
}

class MemoryUsuarioRepository : UsuarioInterface {

    override fun getAllUsuario(): List<Usuario> = transaction {
        UsuariosTable.selectAll().map {
            Usuario(
                email = it[UsuariosTable.email],
                password = it[UsuariosTable.password],
                token = it[UsuariosTable.token]
            )
        }
    }

    override fun getUsuarioByEmail(email: String): Usuario? = transaction {
        // Se utiliza select { ... } en lugar de selectAll().where { ... }
        UsuariosTable.selectAll().where { UsuariosTable.email eq email }
            .map {
                Usuario(
                    email = it[UsuariosTable.email],
                    password = it[UsuariosTable.password],
                    token = it[UsuariosTable.token]
                )
            }
            .firstOrNull()
    }

    override fun postUsuario(usuario: Usuario): Boolean = transaction {
        val insertResult = UsuariosTable.insert {
            it[UsuariosTable.email] = usuario.email
            it[UsuariosTable.password] = usuario.password
            it[UsuariosTable.token] = usuario.token
        }
        insertResult.insertedCount > 0
    }

    override fun updateUsuario(usuario: UpdateUsuario, email: String): Boolean = transaction {
        val updateCount = UsuariosTable.update({ UsuariosTable.email eq email }) {
            usuario.password?.let { newPass -> it[UsuariosTable.password] = newPass }
        }
        updateCount > 0
    }

    override fun deleteUsuario(email: String): Boolean = transaction {
        UsuariosTable.deleteWhere { UsuariosTable.email eq email } > 0
    }

    fun updateToken(email: String, newToken: String): Boolean = transaction {
        UsuariosTable.update({ UsuariosTable.email eq email }) {
            it[UsuariosTable.token] = newToken
        } > 0
    }
}
