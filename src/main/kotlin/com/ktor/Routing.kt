package com.ktor

import io.ktor.server.application.Application
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.auth.*
import com.domain.usecase.*
import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.models.usuarios.Usuario

// Función para configurar las rutas de la aplicación
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

        staticResources("/static", "static")

        // Rutas de autenticación (registro y login) sin protección
        authRoutes(registerUseCase, loginUseCase)

        // Rutas protegidas con JWT
        authenticate("auth-jwt") {
            recetaRoutes(
                getAllRecetasUseCase,
                getRecetaByIdUseCase,
                createRecetaUseCase,
                updateRecetaUseCase,
                deleteRecetaUseCase
            )
        }
    }
}

// Función de extensión para las rutas de recetas
fun Route.recetaRoutes(
    getAllRecetasUseCase: GetAllRecetasUseCase,
    getRecetaByIdUseCase: GetRecetaByIdUseCase,
    createRecetaUseCase: CreateRecetaUseCase,
    updateRecetaUseCase: UpdateRecetaUseCase,
    deleteRecetaUseCase: DeleteRecetaUseCase
) {
    route("/recetas") {
        get {
            val recetas = getAllRecetasUseCase()
            if (recetas.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No hay recetas disponibles")
            } else {
                call.respond(recetas)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                return@get
            }
            val receta = getRecetaByIdUseCase(id)
            if (receta == null) {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
            } else {
                call.respond(receta)
            }
        }

        post("/add") {
            val receta = call.receive<Receta>()
            val createdReceta = createRecetaUseCase(receta)
            call.respond(HttpStatusCode.Created, "Receta creada con éxito")
        }

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

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                return@delete
            }
            // Primero obtenemos la receta para poder borrarla
            val recetaToDelete = getRecetaByIdUseCase(id)
            if (recetaToDelete == null) {
                call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada")
                return@delete
            }
            try {
                deleteRecetaUseCase(recetaToDelete)
                call.respond(HttpStatusCode.OK, "Receta eliminada con éxito")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al eliminar la receta")
            }
        }
    }
}

// Función de extensión para las rutas de autenticación
fun Route.authRoutes(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    post("/register") {
        val user = call.receive<Usuario>()
        try {
            // Se obtiene el JWT generado
            val token = registerUseCase(user)
            // Se envía el token al cliente
            call.respond(HttpStatusCode.Created, mapOf("token" to token))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "El usuario ya existe o ha ocurrido un error")
        }
    }

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