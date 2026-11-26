package com.example.ciclospa.data.remote

import com.example.ciclospa.model.ContenidoEducativo
import retrofit2.http.GET
import retrofit2.http.Path

interface ContenidoApiService {
    @GET("api/v1/contenidos")
    suspend fun listarContenidos(): List<ContenidoEducativo>

    @GET("api/v1/contenidos/{id}")
    suspend fun obtenerContenido(@Path("id") id: Long): ContenidoEducativo
}
