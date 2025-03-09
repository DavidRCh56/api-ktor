package com.domain.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val secret = "mySuperSecretKey"
    private const val issuer = "com.appRecetas"
    private const val validityInMs = 36000000L // 10 horas
    val algorithm: Algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    // Genera el token usando el id del usuario (como String)
    fun generateToken(userId: String): Pair<String, String> {
        val tokenId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val token = JWT.create()
            .withIssuer(issuer)
            .withClaim("id", userId.toInt()) // Se guarda como entero
            .withJWTId(tokenId)
            .withExpiresAt(Date(now + validityInMs))
            .sign(algorithm)
        return Pair(token, tokenId)
    }
}
