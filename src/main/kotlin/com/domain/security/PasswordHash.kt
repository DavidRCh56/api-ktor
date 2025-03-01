package com.domain.security

import java.security.MessageDigest

// Objeto para gestionar el hash de contraseñas
object PasswordHash : PasswordHashInterface {

    override fun hash(pass: String): String {
        // Convertimos la contraseña a bytes
        val passArr = pass.toByteArray(Charsets.UTF_8)
        // Obtenemos la instancia de MessageDigest con el algoritmo SHA-256
        val messageDigest = MessageDigest.getInstance("SHA-256")
        // Generamos el hash en formato ByteArray
        val hashByte: ByteArray = messageDigest.digest(passArr)
        // Convertimos el ByteArray a una representación hexadecimal
        return hashByte.joinToString("") { "%02x".format(it) }
    }

    override fun verify(pass: String, passHash: String): Boolean {
        // Comparamos el hash generado con el hash proporcionado
        return hash(pass) == passHash
    }
}