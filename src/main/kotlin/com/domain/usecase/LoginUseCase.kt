package com.domain.usecase

import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash
import com.domain.security.JwtConfig

class LoginUseCase(private val repository: UsuarioInterface) {
    suspend operator fun invoke(username: String, password: String): String? {
        val usuario = repository.getUsuarioByEmail(username)
        return if (usuario != null && PasswordHash.verify(password, usuario.password)) {
            val (newToken, _) = JwtConfig.generateToken(usuario.id.toString())
            repository.updateToken(usuario.email, newToken)
            newToken
        } else {
            null
        }
    }
}
