package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ciclospa.backend.BackendRepository
import com.example.ciclospa.backend.BackendViewModel
import com.example.ciclospa.backend.ContenidoDto
import com.example.ciclospa.data.CicloSpaDatabase
import com.example.ciclospa.model.RegistroBitacora
import com.example.ciclospa.model.Post
import com.example.ciclospa.repository.PostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoberturaExtraTest {

    private suspend fun esperar(
        condicion: () -> Boolean
    ) {
        withTimeout(10000) {
            while (!condicion()) {
                delay(50)
            }
        }
    }

    @Test
    fun probarCoberturaExtraViewModels() = runBlocking {

        val application =
            ApplicationProvider.getApplicationContext<Application>()

        println("INICIO COBERTURA EXTRA")

        // =====================================================
        // 1. REGISTRO VIEWMODEL
        // =====================================================

        val registroVM = RegistroViewModel(application)

        registroVM.onNombreChange("Medlin")
        registroVM.onCorreoChange("medlin@test.cl")
        registroVM.onAceptarTerminosChange(true)

        assertEquals("Medlin", registroVM.estado.value.nombre)
        assertEquals("medlin@test.cl", registroVM.estado.value.correo)
        assertTrue(registroVM.estado.value.aceptaTerminos)

        assertTrue(registroVM.validar())

        registroVM.onFotoTomada("content://foto/extra")

        assertEquals(
            "content://foto/extra",
            registroVM.estado.value.fotoUri
        )

        registroVM.onNombreChange("")

        assertFalse(registroVM.validar())

        assertNotNull(
            registroVM.estado.value.errores.nombre
        )

        registroVM.onNombreChange("Justin")
        registroVM.onCorreoChange("correoincorrecto")
        registroVM.onAceptarTerminosChange(false)

        assertFalse(registroVM.validar())

        assertNotNull(
            registroVM.estado.value.errores.correo
        )

        assertNotNull(
            registroVM.estado.value.errores.aceptaTerminos
        )

        // =====================================================
        // 2. BITACORA VIEWMODEL
        // =====================================================

        val database =
            CicloSpaDatabase.obtenerInstancia(application)

        val dao = database.bitacoraDao()

        // Limpiar datos anteriores
        dao.obtenerTodosLosRegistros().first().forEach {
            dao.eliminarRegistro(it)
        }

        val bitacoraVM = BitacoraViewModel(application)

        assertEquals(
            3,
            bitacoraVM.opcionesCristalizacion.size
        )

        assertEquals(
            "Alto / Helecho marcado",
            bitacoraVM.cristalizacionSeleccionada
        )

        // Día vacío
        bitacoraVM.diaCiclo = ""

        var callback = false

        bitacoraVM.guardarRegistro {
            callback = true
        }

        delay(200)

        assertFalse(callback)

        // Día inválido
        bitacoraVM.diaCiclo = "abc"

        bitacoraVM.guardarRegistro {
            callback = true
        }

        delay(200)

        assertFalse(callback)

        // Día cero
        bitacoraVM.diaCiclo = "0"

        bitacoraVM.guardarRegistro {
            callback = true
        }

        delay(200)

        assertFalse(callback)

        // Guardar registro válido
        bitacoraVM.diaCiclo = "15"
        bitacoraVM.cristalizacionSeleccionada = "Medio"
        bitacoraVM.notas = "Cobertura extra"

        bitacoraVM.fotoUri = "content://foto/extra"

        var guardado = false

        bitacoraVM.guardarRegistro {
            guardado = true
        }

        esperar {
            guardado
        }

        val registros = dao.obtenerTodosLosRegistros().first()

        assertEquals(1, registros.size)
        assertEquals(15, registros.first().diaCiclo)
        assertEquals("Medio", registros.first().cristalizacion)
        assertEquals("Cobertura extra", registros.first().notas)

        assertEquals(
            "content://foto/extra",
            registros.first().fotoUri
        )

        assertEquals("", bitacoraVM.diaCiclo)
        assertEquals("", bitacoraVM.notas)
        assertNull(bitacoraVM.fotoUri)

        // Eliminar registro
        var eliminado = false

        bitacoraVM.eliminarRegistro(registros.first()) {
            eliminado = true
        }

        esperar {
            eliminado
        }

        assertTrue(
            dao.obtenerTodosLosRegistros().first().isEmpty()
        )

        // =====================================================
        // 3. BACKEND VIEWMODEL
        // =====================================================

        val backendRepository = mockk<BackendRepository>()

        val contenido = ContenidoDto(
            id = 1,
            titulo = "Contenido prueba",
            categoria = "Educación",
            descripcion = "Prueba de cobertura",
            publicado = true
        )

        coEvery {
            backendRepository.obtenerContenidos()
        } returns listOf(contenido)

        coEvery {
            backendRepository.crearContenido(any())
        } returns contenido

        coEvery {
            backendRepository.actualizarContenido(any(), any())
        } returns contenido

        coEvery {
            backendRepository.eliminarContenido(any())
        } returns Unit

        val backendVM = BackendViewModel(backendRepository)

        esperar {
            backendVM.contenidos.value.size == 1
        }

        assertEquals(
            "Contenido prueba",
            backendVM.contenidos.value.first().titulo
        )

        // Crear
        backendVM.crearContenido(
            titulo = "Nuevo contenido",
            categoria = "Educación",
            descripcion = "Descripción nueva",
            publicado = true
        )

        esperar {
            !backendVM.cargando.value
        }

        coVerify {
            backendRepository.crearContenido(any())
        }

        // Actualizar
        backendVM.actualizarContenido(
            id = 1,
            titulo = "Contenido actualizado",
            categoria = "Educación",
            descripcion = "Actualización",
            publicado = false
        )

        esperar {
            !backendVM.cargando.value
        }

        coVerify {
            backendRepository.actualizarContenido(1, any())
        }

        // Eliminar
        backendVM.eliminarContenido(1)

        esperar {
            !backendVM.cargando.value
        }

        coVerify {
            backendRepository.eliminarContenido(1)
        }

        // Error de carga
        coEvery {
            backendRepository.obtenerContenidos()
        } throws RuntimeException("Error de prueba")

        backendVM.cargarContenidos()

        esperar {
            backendVM.error.value != null
        }

        assertEquals(
            "Error de prueba",
            backendVM.error.value
        )

        // =====================================================
        // 4. POST VIEWMODEL
        // =====================================================

        val postRepository = mockk<PostRepository>()

        val postSimulado = mockk<Post>()

        coEvery {
            postRepository.obtenerPosts()
        } returns listOf(postSimulado)

        val postVM = PostViewModel(postRepository)

        postVM.cargarPosts()

        esperar {
            !postVM.cargando.value
        }

        assertEquals(
            1,
            postVM.posts.value.size
        )

        coVerify {
            postRepository.obtenerPosts()
        }

        // Simular error de API
        coEvery {
            postRepository.obtenerPosts()
        } throws RuntimeException("API de prueba")

        postVM.cargarPosts()

        esperar {
            postVM.error.value != null
        }

        assertTrue(
            postVM.error.value!!.contains("Error al conectar con la API")
        )

        println("TEST COBERTURA EXTRA FINALIZADO")
    }
}