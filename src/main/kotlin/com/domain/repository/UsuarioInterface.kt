package com.domain.repository

import com.domain.models.usuarios.UpdateUsuario
import com.domain.models.usuarios.Usuario

interface UsuarioInterface {
    fun getAllUsuario(): List<Usuario>
    fun getUsuarioByEmail(email: String): Usuario?
    fun getUsuarioById(id: Int): Usuario?      // <-- Nuevo método
    fun postUsuario(usuario: Usuario): Int?      // Retorna el id generado o null en caso de error
    fun updateUsuario(usuario: UpdateUsuario, email: String): Boolean
    fun deleteUsuario(email: String): Boolean
    fun updateToken(email: String, newToken: String): Boolean
}
