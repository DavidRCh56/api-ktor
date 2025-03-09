package com.domain.usecase

import com.domain.models.usuarios.Usuario
import com.domain.repository.UsuarioInterface
import com.domain.security.PasswordHash
import com.domain.security.JwtConfig

class RegisterUseCase(private val repository: UsuarioInterface) {
    // Retorna el token JWT generado
    suspend operator fun invoke(usuario: Usuario): String {
        val hashedPassword = PasswordHash.hash(usuario.password)
        // Inserta el usuario y obtiene el id generado
        val generatedId = repository.postUsuario(usuario.copy(password = hashedPassword))
            ?: throw Exception("Error al crear usuario")
        // Genera el token usando el id generado (convertido a String)
        val (token, tokenId) = JwtConfig.generateToken(generatedId.toString())
        // Actualiza en la BD el token (almacenando el tokenId)
        repository.updateToken(usuario.email, tokenId)
        return token
    }
}
