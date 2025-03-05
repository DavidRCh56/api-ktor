package com.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
// Importa los casos de uso y repositorios
import com.domain.usecase.*
import com.domain.repository.RecetaInterface
import com.domain.repository.UsuarioInterface
import com.data.repository.recetas.MemoryRecetaRepository
import com.data.repository.recetas.RecetasTable
import com.data.repository.usuarios.MemoryUsuarioRepository
import com.data.repository.usuarios.UsuariosTable

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8089, module = Application::module).start(wait = true)
}

fun Application.module() {

    // Inicializa la conexión a la base de datos
    Database.connect(
        "jdbc:mariadb://localhost:3307/appRecetas",
        driver = "org.mariadb.jdbc.Driver",
        user = "admin",
        password = "admin"
    )

    // Crea las tablas si no existen
    transaction {
        SchemaUtils.create(RecetasTable, UsuariosTable)
    }

    // Configuración para la serialización JSON
    install(ContentNegotiation) {
        json()
    }

    // Reemplaza las implementaciones en memoria (ya no se usarán listas) pero reutiliza los mismos archivos,
    // ahora con código que opera sobre la base de datos.
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
