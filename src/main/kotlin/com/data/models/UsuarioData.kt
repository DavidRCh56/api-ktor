package com.data.models

import com.domain.models.recetas.Receta
import com.domain.models.usuarios.Usuario

object UsuarioData {
    val listaUsuario = mutableListOf<Usuario> (
        Usuario(email = "davir@gmail.com", password = "123456", token = ""),
        Usuario(email = "davierewrr@gmail.com", password = "123456", token = ""),
        Usuario(email = "sadfsfdir@gmail.com", password = "123456", token = ""),
    )
};