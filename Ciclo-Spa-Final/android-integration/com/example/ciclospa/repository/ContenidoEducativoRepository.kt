package com.example.ciclospa.repository

import com.example.ciclospa.data.remote.ContenidoApiService
import com.example.ciclospa.model.ContenidoEducativo

class ContenidoEducativoRepository(private val api: ContenidoApiService) {
    suspend fun listar(): Result<List<ContenidoEducativo>> = runCatching { api.listarContenidos() }
    suspend fun obtener(id: Long): Result<ContenidoEducativo> = runCatching { api.obtenerContenido(id) }
}
