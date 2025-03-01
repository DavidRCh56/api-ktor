package com.domain.usecase

import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash

class LoginUseCase(private val repository: UsuarioInterface) {
    suspend operator fun invoke(username: String, password: String): Usuario? {
        val usuario = repository.getUsuarioByEmail(username)
        return if (usuario != null && PasswordHash.verify(password, usuario.password)) { // Comparar con el hash
            usuario
        } else {
            null
        }
    }
}
