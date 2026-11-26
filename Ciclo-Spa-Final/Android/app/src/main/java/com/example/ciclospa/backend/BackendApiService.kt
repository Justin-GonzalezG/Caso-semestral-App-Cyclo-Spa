
package com.example.ciclospa.backend

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Path

interface BackendApiService {

    // Obtener todos los contenidos
    @GET("api/v1/contenidos")
    suspend fun obtenerContenidos(): List<ContenidoDto>

    // Obtener contenido por ID
    @GET("api/v1/contenidos/{id}")
    suspend fun obtenerContenido(
        @Path("id") id: Int
    ): ContenidoDto

    // Registrar contenido
    @POST("api/v1/contenidos")
    suspend fun crearContenido(
        @Body contenido: ContenidoDto
    ): ContenidoDto

    // Actualizar contenido
    @PUT("api/v1/contenidos/{id}")
    suspend fun actualizarContenido(
        @Path("id") id: Int,
        @Body contenido: ContenidoDto
    ): ContenidoDto

    // Eliminar contenido
    @DELETE("api/v1/contenidos/{id}")
    suspend fun eliminarContenido(
        @Path("id") id: Int
    )
}
