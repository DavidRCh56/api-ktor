package com.domain.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val secret = "mySuperSecretKey" // Cambia esta clave por una más segura en producción
    private const val issuer = "com.appRecetas"
    private const val validityInMs = 36000000L // 10 horas

    val algorithm: Algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    /**
     * Genera un token JWT que incluye un claim 'jti' (JWT ID) que usaremos para comparar.
     *
     * @return Pair<token, tokenId> donde token es el JWT completo y tokenId es el valor del claim 'jti'
     */
    fun generateToken(userEmail: String): Pair<String, String> {
        val tokenId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val token = JWT.create()
            .withIssuer(issuer)
            .withClaim("email", userEmail)
            .withJWTId(tokenId)  // Se agrega el tokenId como 'jti'
            .withExpiresAt(Date(now + validityInMs))
            .sign(algorithm)
        return Pair(token, tokenId)
    }
}
