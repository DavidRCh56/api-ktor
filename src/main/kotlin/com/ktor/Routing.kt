package com.ktor

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import com.domain.usecase.*
import com.domain.models.recetas.Receta
import com.domain.models.recetas.UpdateReceta
import com.domain.models.usuarios.Usuario
import com.domain.security.JwtConfig
import com.auth0.jwt.exceptions.JWTVerificationException
import com.domain.repository.UsuarioInterface
import kotlinx.serialization.Serializable

fun verifyToken(token: String, usuarioRepository: UsuarioInterface): Int? {
    return try {
        val decoded = JwtConfig.verifier.verify(token)
        val userId = decoded.getClaim("id").asInt()
        // Se obtiene el usuario por su ID y se compara el token almacenado con el token recibido
        val usuario = usuarioRepository.getUsuarioById(userId)
        if (usuario?.token == token) userId else null
    } catch (e: JWTVerificationException) {
        e.printStackTrace()
        null
    }
}

fun Application.configureRouting(
    getAllRecetasUseCase: GetAllRecetasUseCase,
    getRecetaByIdUseCase: GetRecetaByIdUseCase,
    createRecetaUseCase: CreateRecetaUseCase,
    updateRecetaUseCase: UpdateRecetaUseCase,
    deleteRecetaUseCase: DeleteRecetaUseCase,
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase,
    usuarioInterface: UsuarioInterface     // Se inyecta el repositorio de usuarios
) {
    routing {
        get("/") {
            call.respondText("Servidor iniciado correctamente!")
        }
        staticResources("/static", "static")

        // Endpoints públicos: registro y login

        post("/register") {
            val user = call.receive<Usuario>()
            try {
                registerUseCase(user)
                call.respond(HttpStatusCode.Created, "Usuario creado con éxito")
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

        // Endpoints protegidos para el CRUD de recetas
        route("/recetas") {

            // GET: Obtener todas las recetas del usuario autenticado
            get {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ") ?: ""
                val userId = verifyToken(token, usuarioInterface)
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                    return@get
                }
                val recetas = getAllRecetasUseCase().filter { it.userId == userId }
                call.respond(recetas)
            }

            // GET: Obtener una receta por ID (solo si pertenece al usuario autenticado)
            get("/{id}") {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ") ?: ""
                val userId = verifyToken(token, usuarioInterface)
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                    return@get
                }
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                    return@get
                }
                val receta = getRecetaByIdUseCase(id)
                if (receta == null || receta.userId != userId) {
                    call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada o no pertenece al usuario")
                } else {
                    call.respond(receta)
                }
            }

            // POST: Crear una nueva receta
            post("/add") {
                @Serializable
                data class RecetaRequest(val receta: Receta)
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ") ?: ""
                val userId = verifyToken(token, usuarioInterface)
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                    return@post
                }
                val recetaRequest = call.receive<RecetaRequest>()
                // Se asigna el userId obtenido del token a la receta
                val receta = recetaRequest.receta.copy(userId = userId)
                createRecetaUseCase(receta)
                call.respond(HttpStatusCode.Created, "Receta creada con éxito")
            }

            // PUT: Actualizar una receta
            put("/{id}") {
                @Serializable
                data class UpdateRecetaRequest(val updateData: UpdateReceta)
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ") ?: ""
                val userId = verifyToken(token, usuarioInterface)
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                    return@put
                }
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                    return@put
                }
                val existingReceta = getRecetaByIdUseCase(id)
                if (existingReceta == null || existingReceta.userId != userId) {
                    call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada o no pertenece al usuario")
                    return@put
                }
                val updateRequest = call.receive<UpdateRecetaRequest>()
                val updatedReceta = Receta(
                    id = updateRequest.updateData.id ?: id,
                    userId = userId,
                    name = updateRequest.updateData.name ?: existingReceta.name,
                    description = updateRequest.updateData.description ?: existingReceta.description,
                    ingredients = updateRequest.updateData.ingredients ?: existingReceta.ingredients,
                    calories = updateRequest.updateData.calories ?: existingReceta.calories,
                    imageUrl = updateRequest.updateData.imageUrl ?: existingReceta.imageUrl
                )
                val isUpdated = updateRecetaUseCase(id, updatedReceta)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK, "Receta actualizada con éxito")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al actualizar la receta")
                }
            }

            // DELETE: Eliminar una receta
            delete("/{id}") {
                val authHeader = call.request.headers["Authorization"]
                val token = authHeader?.removePrefix("Bearer ") ?: ""
                val userId = verifyToken(token, usuarioInterface)
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                    return@delete
                }
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "El ID proporcionado no es válido")
                    return@delete
                }
                val recetaToDelete = getRecetaByIdUseCase(id)
                if (recetaToDelete == null || recetaToDelete.userId != userId) {
                    call.respond(HttpStatusCode.NotFound, "Receta con ID $id no encontrada o no pertenece al usuario")
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
}