package com.ktor

import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.models.usuarios.Usuario
import com.domain.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*

fun Application.configureRouting(
    getAllRecetasUseCase: GetAllRecetasUseCase,
    getRecetaByIdUseCase: GetRecetaByIdUseCase,
    createRecetaUseCase: CreateRecetaUseCase,
    updateRecetaUseCase: UpdateRecetaUseCase,
    deleteRecetaUseCase: DeleteRecetaUseCase,
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    routing {
        get("/") {
            call.respondText("Servidor iniciado correctamente!")
        }

        // Recursos estáticos
        staticResources("/static", "static")

        // Rutas de recetas
        recetaRoutes(
            getAllRecetasUseCase,
            getRecetaByIdUseCase,
            createRecetaUseCase,
            updateRecetaUseCase,
            deleteRecetaUseCase
        )

        // Rutas de autenticación de usuario
        authRoutes(registerUseCase, loginUseCase)
    }
}

// Definición de las rutas para recetas (mantiene las anteriores)
fun Route.recetaRoutes(
    getAllRecetasUseCase: GetAllRecetasUseCase,
    getRecetaByIdUseCase: GetRecetaByIdUseCase,
    createRecetaUseCase: CreateRecetaUseCase,
    updateRecetaUseCase: UpdateRecetaUseCase,
    deleteRecetaUseCase: DeleteRecetaUseCase
) {
    route("/recetas") {

        // Obtener todas las recetas
        get {
            val recetas = getAllRecetasUseCase()
            if (recetas.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No hay recetas disponibles")
            } else {
                call.respond(recetas)
            }
        }

        // Obtener una receta por ID
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                return@get
            }
            val recetas = getRecetaByIdUseCase(id)
            if (recetas == null) {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
            } else {
                call.respond(recetas)
            }
        }

        // Crear una nueva receta
        post("/add") {
            val receta = call.receive<Receta>()
            val createdReceta = createRecetaUseCase(receta)
            if (createdReceta != null) {
                call.respond(HttpStatusCode.Created, "Receta creada con éxito")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Error al crear la receta")
            }
        }

        // Actualizar una receta existente
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                return@put
            }
            val updateData = call.receive<UpdateReceta>()
            val isUpdated = updateRecetaUseCase(
                id,
                Receta(
                    id = updateData.id ?: id,
                    userId = updateData.userId ?: "",
                    name = updateData.name ?: "",
                    description = updateData.description ?: "",
                    ingredients = updateData.ingredients ?: "",
                    calories = updateData.calories ?: "",
                    imageUrl = updateData.imageUrl
                )
            )
            if (isUpdated) {
                call.respond(HttpStatusCode.OK, "Receta actualizada con éxito")
            } else {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
            }
        }

        // Eliminar una receta por ID
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                return@delete
            }
            val recetaToDelete = getRecetaByIdUseCase(id)
            if (recetaToDelete == null) {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
                return@delete
            }
            val isDeleted = try {
                deleteRecetaUseCase(recetaToDelete)
                true
            } catch (e: Exception) {
                false
            }
            if (isDeleted) {
                call.respond(HttpStatusCode.OK, "Receta eliminada con éxito")
            } else {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
            }
        }
    }
}

// Rutas para Login y Registro
fun Route.authRoutes(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    // Registro de usuario
    post("/register") {
        val user = call.receive<Usuario>()
        val isRegistered = try {
            registerUseCase(user)
            true
        } catch (e: Exception) {
            false
        }
        if (isRegistered) {
            call.respond(HttpStatusCode.Created, "Usuario registrado con éxito")
        } else {
            call.respond(HttpStatusCode.Conflict, "El usuario ya existe")
        }
    }

    // Login de usuario
    post("/login") {
        val user = call.receive<Usuario>()
        val token = loginUseCase(user.email, user.password)
        if (token != null) {
            call.respond(HttpStatusCode.OK, mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
        }
    }
}