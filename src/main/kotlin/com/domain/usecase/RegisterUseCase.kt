package com.domain.usecase

import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash

class RegisterUseCase(private val repository: UsuarioInterface) {
    suspend operator fun invoke(usuario: Usuario) {
        val hashedPassword = PasswordHash.hash(usuario.password)
        repository.postUsuario(usuario.copy(password = hashedPassword))
            ?: throw Exception("Error al crear usuario")
    }
}
