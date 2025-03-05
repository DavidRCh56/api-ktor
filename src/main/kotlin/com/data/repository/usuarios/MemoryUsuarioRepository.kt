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
    override val primaryKey = PrimaryKey(id)
}

class MemoryUsuarioRepository : UsuarioInterface {

    override fun getAllUsuario(): List<Usuario> = transaction {
        UsuariosTable.selectAll().map {
            Usuario(
                email = it[UsuariosTable.email],
                password = it[UsuariosTable.password]
            )
        }
    }

    override fun getUsuarioByEmail(email: String): Usuario? = transaction {
        UsuariosTable.selectAll().where { UsuariosTable.email eq email }
            .map {
                Usuario(
                    email = it[UsuariosTable.email],
                    password = it[UsuariosTable.password]
                )
            }
            .firstOrNull()
    }

    override fun postUsuario(usuario: Usuario): Boolean = transaction {
        val insertResult = UsuariosTable.insert {
            it[UsuariosTable.email] = usuario.email
            it[UsuariosTable.password] = usuario.password
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
}
