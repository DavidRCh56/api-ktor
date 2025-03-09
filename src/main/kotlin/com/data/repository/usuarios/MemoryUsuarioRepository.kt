package com.data.repository.usuarios

import com.domain.models.usuarios.Usuario
import com.domain.models.usuarios.UpdateUsuario
import com.domain.repository.UsuarioInterface
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

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
                id = it[UsuariosTable.id],
                email = it[UsuariosTable.email],
                password = it[UsuariosTable.password],
                token = it[UsuariosTable.token]
            )
        }
    }

    override fun getUsuarioByEmail(email: String): Usuario? = transaction {
        UsuariosTable.selectAll().where { UsuariosTable.email eq email }
            .map {
                Usuario(
                    id = it[UsuariosTable.id],
                    email = it[UsuariosTable.email],
                    password = it[UsuariosTable.password],
                    token = it[UsuariosTable.token]
                )
            }
            .firstOrNull()
    }

    // Inserta el usuario y retorna el id generado
    override fun postUsuario(usuario: Usuario): Int? = transaction {
        val insertResult = UsuariosTable.insert {
            it[email] = usuario.email
            it[password] = usuario.password
            it[token] = usuario.token
        }
        insertResult.resultedValues?.firstOrNull()?.get(UsuariosTable.id)
    }

    override fun updateUsuario(usuario: UpdateUsuario, email: String): Boolean = transaction {
        val updateCount = UsuariosTable.update({ UsuariosTable.email eq email }) {
            usuario.password?.let { newPass -> it[password] = newPass }
        }
        updateCount > 0
    }

    override fun deleteUsuario(email: String): Boolean = transaction {
        UsuariosTable.deleteWhere { UsuariosTable.email eq email } > 0
    }

    override fun updateToken(email: String, newToken: String): Boolean = transaction {
        UsuariosTable.update({ UsuariosTable.email eq email }) {
            it[token] = newToken
        } > 0
    }
}
