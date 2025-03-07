package com.domain.usecase

import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash
import com.domain.security.JwtConfig

class RegisterUseCase(private val repository: UsuarioInterface) {
    // Ahora retorna el token JWT
    suspend operator fun invoke(usuario: Usuario): String {
        val hashedPassword = PasswordHash.hash(usuario.password)
        // Genera token y extrae tokenId
        val (token, tokenId) = JwtConfig.generateToken(usuario.email)
        // Se almacena el tokenId en la BBDD
        val usuarioConHash = usuario.copy(password = hashedPassword, token = tokenId)
        if (repository.postUsuario(usuarioConHash)) {
            return token
        } else {
            throw Exception("Failed to create usuario")
        }
    }
}
