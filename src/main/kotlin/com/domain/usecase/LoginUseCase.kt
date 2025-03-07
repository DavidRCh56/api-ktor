package com.domain.usecase

import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash
import com.domain.security.JwtConfig

class LoginUseCase(private val repository: UsuarioInterface) {
    suspend operator fun invoke(username: String, password: String): String? {
        val usuario = repository.getUsuarioByEmail(username)
        return if (usuario != null && PasswordHash.verify(password, usuario.password)) {
            // Genera token y extrae tokenId
            val (newToken, tokenId) = JwtConfig.generateToken(username)
            // Actualiza el token en la BBDD (almacenamos el tokenId)
            if (repository is com.data.repository.usuarios.MemoryUsuarioRepository) {
                repository.updateToken(username, tokenId)
            }
            newToken
        } else {
            null
        }
    }
}
