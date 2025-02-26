package com.domain.repository

import com.domain.models.usuarios.UpdateUsuario
import com.domain.models.usuarios.Usuario

interface UsuarioInterface {
    fun getAllUsuario () : List <Usuario>

    fun getUsuarioByEmail (email: String) : Usuario?

    fun postUsuario(usuario: Usuario) : Boolean

    fun updateUsuario(usuario: UpdateUsuario, email: String) : Boolean

    fun deleteUsuario(email : String) : Boolean
}