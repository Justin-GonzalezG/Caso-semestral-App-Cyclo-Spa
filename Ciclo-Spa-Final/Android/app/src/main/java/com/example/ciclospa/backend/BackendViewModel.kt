
package com.example.ciclospa.backend

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BackendViewModel(
    private val repository: BackendRepository = BackendRepository()
) : ViewModel() {

    val contenidos = mutableStateOf<List<ContenidoDto>>(emptyList())
    val cargando = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        cargarContenidos()
    }

    fun cargarContenidos() {
        viewModelScope.launch {
            cargando.value = true
            error.value = null

            try {
                contenidos.value = repository.obtenerContenidos()
            } catch (e: Exception) {
                error.value = e.message ?: "Error al consultar contenidos"
            } finally {
                cargando.value = false
            }
        }
    }

    fun crearContenido(
        titulo: String,
        categoria: String,
        descripcion: String,
        publicado: Boolean
    ) {
        viewModelScope.launch {
            cargando.value = true
            error.value = null

            try {
                val nuevo = ContenidoDto(
                    id = 0,
                    titulo = titulo,
                    categoria = categoria,
                    descripcion = descripcion,
                    publicado = publicado
                )

                repository.crearContenido(nuevo)
                cargarContenidos()

            } catch (e: Exception) {
                error.value = e.message ?: "Error al crear contenido"
            } finally {
                cargando.value = false
            }
        }
    }

    fun actualizarContenido(
        id: Int,
        titulo: String,
        categoria: String,
        descripcion: String,
        publicado: Boolean
    ) {
        viewModelScope.launch {
            cargando.value = true
            error.value = null

            try {
                val actualizado = ContenidoDto(
                    id = id,
                    titulo = titulo,
                    categoria = categoria,
                    descripcion = descripcion,
                    publicado = publicado
                )

                repository.actualizarContenido(id, actualizado)
                cargarContenidos()

            } catch (e: Exception) {
                error.value = e.message ?: "Error al actualizar contenido"
            } finally {
                cargando.value = false
            }
        }
    }

    fun eliminarContenido(id: Int) {
        viewModelScope.launch {
            cargando.value = true
            error.value = null

            try {
                repository.eliminarContenido(id)
                cargarContenidos()

            } catch (e: Exception) {
                error.value = e.message ?: "Error al eliminar contenido"
            } finally {
                cargando.value = false
            }
        }
    }
}
