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
import kotlinx.serialization.Serializable

// Para recibir solo el token en endpoints GET (y otros)
@Serializable
data class TokenRequest(val token: String)

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
        // Rutas públicas: registro y login
        authRoutes(registerUseCase, loginUseCase)
        // Rutas para el CRUD de recetas
        recetaRoutes(
            getAllRecetasUseCase,
            getRecetaByIdUseCase,
            createRecetaUseCase,
            updateRecetaUseCase,
            deleteRecetaUseCase
        )
    }
}

fun verifyToken(token: String): Int? {
    return try {
        val decoded = JwtConfig.verifier.verify(token)
        decoded.getClaim("id").asInt()
    } catch (e: JWTVerificationException) {
        e.printStackTrace()
        null
    }
}


fun Route.authRoutes(
    registerUseCase: RegisterUseCase,
    loginUseCase: LoginUseCase
) {
    post("/register") {
        val user = call.receive<Usuario>()
        try {
            val token = registerUseCase(user)
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

fun Route.recetaRoutes(
    getAllRecetasUseCase: GetAllRecetasUseCase,
    getRecetaByIdUseCase: GetRecetaByIdUseCase,
    createRecetaUseCase: CreateRecetaUseCase,
    updateRecetaUseCase: UpdateRecetaUseCase,
    deleteRecetaUseCase: DeleteRecetaUseCase
) {
    route("/recetas") {

        // GET: Obtener todas las recetas del usuario autenticado
        get {
            val tokenRequest = call.receive<TokenRequest>()
            val userId = verifyToken(tokenRequest.token)
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                return@get
            }
            // Filtra las recetas cuyo userId coincide con el id obtenido del token
            val recetas = getAllRecetasUseCase().filter { it.userId == userId }
            call.respond(recetas)
        }

        // GET: Obtener una receta por ID (solo si pertenece al usuario autenticado)
        get("/{id}") {
            val tokenRequest = call.receive<TokenRequest>()
            val userId = verifyToken(tokenRequest.token)
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

        // POST: Crear una nueva receta (requiere token y datos de la receta)
        post("/add") {
            @Serializable
            data class RecetaRequest(val token: String, val receta: Receta)
            val recetaRequest = call.receive<RecetaRequest>()
            val userId = verifyToken(recetaRequest.token)
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Acceso no autorizado")
                return@post
            }
            // Se asigna el userId obtenido del token a la receta
            val receta = recetaRequest.receta.copy(userId = userId)
            createRecetaUseCase(receta)
            call.respond(HttpStatusCode.Created, "Receta creada con éxito")
        }

        // PUT: Actualizar una receta (requiere token y datos de actualización)
        put("/{id}") {
            @Serializable
            data class UpdateRecetaRequest(val token: String, val updateData: UpdateReceta)
            val updateRequest = call.receive<UpdateRecetaRequest>()
            val userId = verifyToken(updateRequest.token)
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

        // DELETE: Eliminar una receta (requiere token)
        delete("/{id}") {
            @Serializable
            data class DeleteRecetaRequest(val token: String)
            val deleteRequest = call.receive<DeleteRecetaRequest>()
            val userId = verifyToken(deleteRequest.token)
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
