package com.data.repository.usuarios

import com.domain.models.usuarios.UpdateUsuario
import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface


class DatabaseUsuarioRepository : UsuarioInterface {
    override fun getAllUsuario(): List<Usuario> {
        TODO("Not yet implemented")
    }

    override fun getUsuarioByEmail(email: String): Usuario? {
        TODO("Not yet implemented")
    }

    override fun postUsuario(usuario: Usuario): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateUsuario(usuario: UpdateUsuario, email: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteUsuario(email: String): Boolean {
        TODO("Not yet implemented")
    }
}