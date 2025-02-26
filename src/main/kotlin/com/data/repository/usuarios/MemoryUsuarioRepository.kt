package com.data.repository.usuarios

import com.data.models.UsuarioData
import com.domain.models.usuarios.UpdateUsuario
import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface

class MemoryUsuarioRepository : UsuarioInterface {
    override fun getAllUsuario(): List<Usuario> {
        return UsuarioData.listaUsuario
    }

    override fun getUsuarioByEmail(email: String): Usuario? {
        return UsuarioData.listaUsuario.find { it.email == email }
    }

    override fun postUsuario(usuario: Usuario): Boolean {
        return if (UsuarioData.listaUsuario.any { it.email == usuario.email }) {
            false
        } else {
            UsuarioData.listaUsuario.add(usuario)
            true
        }
    }

    override fun updateUsuario(usuario: UpdateUsuario, email: String): Boolean {
        val index = UsuarioData.listaUsuario.indexOfFirst { it.email == email }
        if (index == -1) return false

        val usuarioExistente = UsuarioData.listaUsuario[index]
        val usuarioActualizado = usuarioExistente.copy(
            password = usuario.password ?: usuarioExistente.password
        )

        UsuarioData.listaUsuario[index] = usuarioActualizado
        return true
    }

    override fun deleteUsuario(email: String): Boolean {
        return UsuarioData.listaUsuario.removeIf { it.email == email }
    }
}
