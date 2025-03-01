package com.ktor

import com.data.repository.recetas.MemoryRecetaRepository
import com.data.repository.usuarios.MemoryUsuarioRepository
import com.domain.repository.RecetaInterface
import com.domain.repository.UsuarioInterface
import com.domain.usecase.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8089, module = Application::module).start(wait = true)
}

fun Application.module() {

    // Configuración para la serialización JSON
    configureSerialization()

    // Implementación de interfaces/repositories
    val recetaInterface: RecetaInterface = MemoryRecetaRepository()
    val usuarioInterface: UsuarioInterface = MemoryUsuarioRepository()

    // Casos de uso para recetas
    val getAllRecetasUseCase = GetAllRecetasUseCase(recetaInterface)
    val getRecetaByIdUseCase = GetRecetaByIdUseCase(recetaInterface)
    val createRecetaUseCase = CreateRecetaUseCase(recetaInterface)
    val updateRecetaUseCase = UpdateRecetaUseCase(recetaInterface)
    val deleteRecetaUseCase = DeleteRecetaUseCase(recetaInterface)

    // Casos de uso para usuarios
    val registerUseCase = RegisterUseCase(usuarioInterface)
    val loginUseCase = LoginUseCase(usuarioInterface)

    // Configuración de rutas
    configureRouting(
        getAllRecetasUseCase,
        getRecetaByIdUseCase,
        createRecetaUseCase,
        updateRecetaUseCase,
        deleteRecetaUseCase,
        registerUseCase,
        loginUseCase
    )
}