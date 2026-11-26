
package com.example.ciclospa.backend

class BackendRepository(
    private val apiService: BackendApiService = BackendRetrofit.apiService
) {

    suspend fun obtenerContenidos(): List<ContenidoDto> {
        return apiService.obtenerContenidos()
    }

    suspend fun obtenerContenido(id: Int): ContenidoDto {
        return apiService.obtenerContenido(id)
    }

    suspend fun crearContenido(contenido: ContenidoDto): ContenidoDto {
        return apiService.crearContenido(contenido)
    }

    suspend fun actualizarContenido(
        id: Int,
        contenido: ContenidoDto
    ): ContenidoDto {
        return apiService.actualizarContenido(id, contenido)
    }

    suspend fun eliminarContenido(id: Int) {
        apiService.eliminarContenido(id)
    }
}
