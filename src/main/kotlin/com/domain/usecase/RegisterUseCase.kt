package com.domain.usecase

import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash

class RegisterUseCase(private val repository: UsuarioInterface) {
    suspend operator fun invoke(usuario: Usuario): Usuario {
        val hashedPassword = PasswordHash.hash(usuario.password)
        val usuarioConHash = usuario.copy(password = hashedPassword)
        if (repository.postUsuario(usuarioConHash)) {
            return usuarioConHash
        } else {
            throw Exception("Failed to create usuario")
        }
    }
}
