package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ViewModelsCoberturaTest {

    private lateinit var application: Application

    @Before
    fun preparar() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        application = ApplicationProvider.getApplicationContext()
    }

    @After
    fun finalizar() {
        Dispatchers.resetMain()
    }

    // ==========================================
    // PRUEBAS REGISTRO VIEWMODEL
    // ==========================================

    @Test
    fun deberia_actualizar_nombre() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")

        assertEquals("Medlin", vm.estado.value.nombre)
    }

    @Test
    fun deberia_actualizar_correo() {
        val vm = RegistroViewModel(application)

        vm.onCorreoChange("medlin@gmail.com")

        assertEquals("medlin@gmail.com", vm.estado.value.correo)
    }

    @Test
    fun deberia_actualizar_terminos() {
        val vm = RegistroViewModel(application)

        vm.onAceptarTerminosChange(true)

        assertTrue(vm.estado.value.aceptaTerminos)
    }

    @Test
    fun deberia_actualizar_foto() {
        val vm = RegistroViewModel(application)

        vm.onFotoTomada("content://foto/test.jpg")

        assertEquals("content://foto/test.jpg", vm.estado.value.fotoUri)
    }

    @Test
    fun deberia_rechazar_formulario_vacio() {
        val vm = RegistroViewModel(application)

        assertFalse(vm.validar())

        assertEquals(
            "El nombre es obligatorio",
            vm.estado.value.errores.nombre
        )

        assertEquals(
            "El correo es obligatorio",
            vm.estado.value.errores.correo
        )

        assertEquals(
            "Debes aceptar los términos",
            vm.estado.value.errores.aceptaTerminos
        )
    }

    @Test
    fun deberia_rechazar_correo_invalido() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("correo-invalido")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())

        assertEquals(
            "Ingresa un correo válido",
            vm.estado.value.errores.correo
        )
    }

    @Test
    fun deberia_rechazar_nombre_vacio() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange(" ")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())

        assertEquals(
            "El nombre es obligatorio",
            vm.estado.value.errores.nombre
        )
    }

    @Test
    fun deberia_rechazar_terminos_no_aceptados() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(false)

        assertFalse(vm.validar())

        assertEquals(
            "Debes aceptar los términos",
            vm.estado.value.errores.aceptaTerminos
        )
    }

    @Test
    fun deberia_validar_formulario_correcto() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(true)

        assertTrue(vm.validar())

        assertNull(vm.estado.value.errores.nombre)
        assertNull(vm.estado.value.errores.correo)
        assertNull(vm.estado.value.errores.aceptaTerminos)
    }

    // ==========================================
    // PRUEBAS BITACORA VIEWMODEL
    // ==========================================

    @Test
    fun deberia_iniciar_bitacora_con_campos_iniciales() {
        val vm = BitacoraViewModel(application)

        assertEquals("", vm.diaCiclo)
        assertEquals("Alto / Helecho marcado", vm.cristalizacionSeleccionada)
        assertEquals("", vm.notas)
        assertNull(vm.fotoUri)
    }

    @Test
    fun deberia_tener_tres_opciones_cristalizacion() {
        val vm = BitacoraViewModel(application)

        assertEquals(3, vm.opcionesCristalizacion.size)

        assertTrue(
            vm.opcionesCristalizacion.contains("Alto / Helecho marcado")
        )

        assertTrue(
            vm.opcionesCristalizacion.contains("Medio")
        )

        assertTrue(
            vm.opcionesCristalizacion.contains("Nulo")
        )
    }

    @Test
    fun deberia_rechazar_dia_ciclo_vacio() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = ""

        var guardado = false

        vm.guardarRegistro {
            guardado = true
        }

        assertFalse(guardado)
    }

    @Test
    fun deberia_rechazar_dia_ciclo_no_numerico() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "abc"

        var guardado = false

        vm.guardarRegistro {
            guardado = true
        }

        assertFalse(guardado)
    }

    @Test
    fun deberia_rechazar_dia_ciclo_cero() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "0"

        var guardado = false

        vm.guardarRegistro {
            guardado = true
        }

        assertFalse(guardado)
    }

    @Test
    fun deberia_rechazar_dia_ciclo_negativo() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "-1"

        var guardado = false

        vm.guardarRegistro {
            guardado = true
        }

        assertFalse(guardado)
    }
}