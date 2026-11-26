package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class RegistroViewModelTest {

    private lateinit var application: Application

    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        application = ApplicationProvider.getApplicationContext()
    }

    @After
    fun finalizar() {
        Dispatchers.resetMain()
    }

    @Test
    fun deberia_iniciar_formulario() {
        val vm = RegistroViewModel(application)

        assertFalse(vm.estado.value.guardado)
        assertFalse(vm.estado.value.guardando)
    }

    @Test
    fun deberia_cambiar_nombre_y_limpiar_error() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")

        assertEquals("Medlin", vm.estado.value.nombre)
    }

    @Test
    fun deberia_cambiar_correo() {
        val vm = RegistroViewModel(application)

        vm.onCorreoChange("medlin@gmail.com")

        assertEquals("medlin@gmail.com", vm.estado.value.correo)
    }

    @Test
    fun deberia_aceptar_terminos() {
        val vm = RegistroViewModel(application)

        vm.onAceptarTerminosChange(true)

        assertTrue(vm.estado.value.aceptaTerminos)
    }

    @Test
    fun deberia_rechazar_terminos() {
        val vm = RegistroViewModel(application)

        vm.onAceptarTerminosChange(false)

        assertFalse(vm.estado.value.aceptaTerminos)
    }

    @Test
    fun deberia_actualizar_foto_uri() {
        val vm = RegistroViewModel(application)

        vm.onFotoTomada("content://foto/registro.jpg")

        assertEquals(
            "content://foto/registro.jpg",
            vm.estado.value.fotoUri
        )
    }

    @Test
    fun deberia_validar_correo_con_dominio() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@example.com")
        vm.onAceptarTerminosChange(true)

        assertTrue(vm.validar())
    }

    @Test
    fun deberia_validar_correo_con_subdominio() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@mail.example.com")
        vm.onAceptarTerminosChange(true)

        assertTrue(vm.validar())
    }

    @Test
    fun deberia_rechazar_correo_sin_arroba() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlinmail.com")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())
    }

    @Test
    fun deberia_rechazar_correo_sin_dominio() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())
    }

    @Test
    fun deberia_rechazar_correo_vacio() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())
    }

    @Test
    fun deberia_rechazar_nombre_solo_espacios() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("   ")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(true)

        assertFalse(vm.validar())
    }

    @Test
    fun deberia_rechazar_formulario_sin_terminos() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(false)

        assertFalse(vm.validar())
    }

    @Test
    fun deberia_validar_formulario_completo() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")
        vm.onCorreoChange("medlin@gmail.com")
        vm.onAceptarTerminosChange(true)

        assertTrue(vm.validar())
    }

    @Test
    fun deberia_limpiar_error_nombre_al_cambiar_nombre() {
        val vm = RegistroViewModel(application)

        vm.validar()

        vm.onNombreChange("Nuevo nombre")

        assertNull(vm.estado.value.errores.nombre)
    }

    @Test
    fun deberia_limpiar_error_correo_al_cambiar_correo() {
        val vm = RegistroViewModel(application)

        vm.validar()

        vm.onCorreoChange("nuevo@gmail.com")

        assertNull(vm.estado.value.errores.correo)
    }

    @Test
    fun deberia_limpiar_error_terminos_al_aceptar() {
        val vm = RegistroViewModel(application)

        vm.validar()

        vm.onAceptarTerminosChange(true)

        assertNull(vm.estado.value.errores.aceptaTerminos)
    }

    @Test
    fun deberia_marcar_formulario_como_no_guardado_al_cambiar_nombre() {
        val vm = RegistroViewModel(application)

        vm.onNombreChange("Medlin")

        assertFalse(vm.estado.value.guardado)
    }

    @Test
    fun deberia_marcar_formulario_como_no_guardado_al_cambiar_correo() {
        val vm = RegistroViewModel(application)

        vm.onCorreoChange("medlin@gmail.com")

        assertFalse(vm.estado.value.guardado)
    }
}