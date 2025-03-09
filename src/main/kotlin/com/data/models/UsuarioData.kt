package com.data.models

import com.domain.models.usuarios.Usuario

object UsuarioData {
    val listaUsuario = mutableListOf<Usuario>(
        Usuario(id = 1, email = "davir@gmail.com", password = "123456", token = ""),
        Usuario(id = 2, email = "davierewrr@gmail.com", password = "123456", token = ""),
        Usuario(id = 3, email = "sadfsfdir@gmail.com", password = "123456", token = "")
    )
}
