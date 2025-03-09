package com.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.domain.usecase.*
import com.domain.repository.RecetaInterface
import com.domain.repository.UsuarioInterface
import com.data.repository.recetas.MemoryRecetaRepository
import com.data.repository.recetas.RecetasTable
import com.data.repository.usuarios.MemoryUsuarioRepository
import com.data.repository.usuarios.UsuariosTable
import com.ktor.configureRouting

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8089, module = Application::module).start(wait = true)
}

fun Application.module() {
    // Conexión a la base de datos
    Database.connect(
        "jdbc:mariadb://localhost:3307/appRecetas",
        driver = "org.mariadb.jdbc.Driver",
        user = "admin",
        password = "admin"
    )

    // Creación de las tablas
    transaction {
        SchemaUtils.create(RecetasTable, UsuariosTable)
    }

    // Configuración de serialización JSON
    install(ContentNegotiation) {
        json()
    }

    // Instanciar repositorios
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
