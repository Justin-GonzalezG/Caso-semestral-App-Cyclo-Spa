
package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ciclospa.data.CicloSpaDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewModelsCoberturaTest {

    @Test
    fun probarRegistroYBitacoraViewModels() = runBlocking {

        val application =
            ApplicationProvider.getApplicationContext<Application>()

        // =====================================================
        // REGISTRO VIEWMODEL
        // =====================================================

        val registroViewModel = RegistroViewModel(application)

        // 1. Campos vacíos
        registroViewModel.onNombreChange("")
        registroViewModel.onCorreoChange("")
        registroViewModel.onAceptarTerminosChange(false)

        assertFalse(registroViewModel.validar())

        assertNotNull(registroViewModel.estado.value.errores.nombre)
        assertNotNull(registroViewModel.estado.value.errores.correo)
        assertNotNull(
            registroViewModel.estado.value.errores.aceptaTerminos
        )

        // 2. Correo inválido
        registroViewModel.onNombreChange("Medlin")
        registroViewModel.onCorreoChange("correo-invalido")
        registroViewModel.onAceptarTerminosChange(true)

        assertFalse(registroViewModel.validar())
        assertNotNull(registroViewModel.estado.value.errores.correo)

        // 3. Datos válidos
        registroViewModel.onNombreChange("Medlin")
        registroViewModel.onCorreoChange("medlin_test_vm@test.cl")
        registroViewModel.onAceptarTerminosChange(true)

        assertTrue(registroViewModel.validar())

        assertNull(registroViewModel.estado.value.errores.nombre)
        assertNull(registroViewModel.estado.value.errores.correo)
        assertNull(
            registroViewModel.estado.value.errores.aceptaTerminos
        )

        // 4. Cambiar nombre
        registroViewModel.onNombreChange("Justin")

        assertEquals(
            "Justin",
            registroViewModel.estado.value.nombre
        )

        // 5. Cambiar correo
        registroViewModel.onCorreoChange("justin@test.cl")

        assertEquals(
            "justin@test.cl",
            registroViewModel.estado.value.correo
        )

        // 6. Cambiar términos
        registroViewModel.onAceptarTerminosChange(false)

        assertFalse(
            registroViewModel.estado.value.aceptaTerminos
        )

        // 7. Foto
        registroViewModel.onFotoTomada("content://foto/test")

        assertEquals(
            "content://foto/test",
            registroViewModel.estado.value.fotoUri
        )

        // =====================================================
        // BITACORA VIEWMODEL
        // =====================================================

        val database =
            CicloSpaDatabase.obtenerInstancia(application)

        val bitacoraDao = database.bitacoraDao()

        // Limpiar registros anteriores
        val registrosAnteriores =
            bitacoraDao.obtenerTodosLosRegistros().first()

        registrosAnteriores.forEach {
            bitacoraDao.eliminarRegistro(it)
        }

        val bitacoraViewModel = BitacoraViewModel(application)

        // 8. Verificar opciones
        assertEquals(
            3,
            bitacoraViewModel.opcionesCristalizacion.size
        )

        assertEquals(
            "Alto / Helecho marcado",
            bitacoraViewModel.cristalizacionSeleccionada
        )

        // 9. Día vacío
        bitacoraViewModel.diaCiclo = ""

        var callbackEjecutado = false

        bitacoraViewModel.guardarRegistro {
            callbackEjecutado = true
        }

        delay(300)

        assertFalse(callbackEjecutado)

        // 10. Día con texto
        bitacoraViewModel.diaCiclo = "abc"

        bitacoraViewModel.guardarRegistro {
            callbackEjecutado = true
        }

        delay(300)

        assertFalse(callbackEjecutado)

        // 11. Día igual a cero
        bitacoraViewModel.diaCiclo = "0"

        bitacoraViewModel.guardarRegistro {
            callbackEjecutado = true
        }

        delay(300)

        assertFalse(callbackEjecutado)

        // 12. Guardar registro válido
        bitacoraViewModel.diaCiclo = "12"
        bitacoraViewModel.cristalizacionSeleccionada = "Medio"
        bitacoraViewModel.notas = "Prueba automatizada"
        bitacoraViewModel.fotoUri = "content://foto/bitacora"

        val registroGuardado = CompletableDeferred<Unit>()

        bitacoraViewModel.guardarRegistro {
            registroGuardado.complete(Unit)
        }

        withTimeout(5000) {
            registroGuardado.await()
        }

        // 13. Consultar registro
        val registros =
            bitacoraDao.obtenerTodosLosRegistros().first()

        assertEquals(1, registros.size)
        assertEquals(12, registros.first().diaCiclo)
        assertEquals("Medio", registros.first().cristalizacion)
        assertEquals(
            "Prueba automatizada",
            registros.first().notas
        )

        assertEquals(
            "content://foto/bitacora",
            registros.first().fotoUri
        )

        // 14. Verificar limpieza
        assertEquals("", bitacoraViewModel.diaCiclo)
        assertEquals("", bitacoraViewModel.notas)
        assertNull(bitacoraViewModel.fotoUri)

        // 15. Eliminar registro
        val registroParaEliminar = registros.first()

        val eliminado = CompletableDeferred<Unit>()

        bitacoraViewModel.eliminarRegistro(
            registroParaEliminar
        ) {
            eliminado.complete(Unit)
        }

        withTimeout(5000) {
            eliminado.await()
        }

        // 16. Verificar eliminación
        val registrosFinales =
            bitacoraDao.obtenerTodosLosRegistros().first()

        assertTrue(registrosFinales.isEmpty())
    }
}
