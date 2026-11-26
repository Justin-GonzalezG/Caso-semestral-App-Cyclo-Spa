package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class BitacoraViewModelTest {

    private lateinit var application: Application
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(testDispatcher)
        application = ApplicationProvider.getApplicationContext()
    }

    @After
    fun finalizar() {
        Dispatchers.resetMain()
    }

    @Test
    fun deberia_cambiar_dia_ciclo() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "12"

        assertEquals("12", vm.diaCiclo)
    }

    @Test
    fun deberia_cambiar_notas() {
        val vm = BitacoraViewModel(application)

        vm.notas = "Observación de prueba"

        assertEquals("Observación de prueba", vm.notas)
    }

    @Test
    fun deberia_cambiar_foto() {
        val vm = BitacoraViewModel(application)

        vm.fotoUri = "content://foto/bitacora.jpg"

        assertEquals("content://foto/bitacora.jpg", vm.fotoUri)
    }

    @Test
    fun deberia_cambiar_cristalizacion() {
        val vm = BitacoraViewModel(application)

        vm.cristalizacionSeleccionada = "Medio"

        assertEquals("Medio", vm.cristalizacionSeleccionada)
    }

    @Test
    fun deberia_seleccionar_cristalizacion_nula() {
        val vm = BitacoraViewModel(application)

        vm.cristalizacionSeleccionada = "Nulo"

        assertEquals("Nulo", vm.cristalizacionSeleccionada)
    }

    @Test
    fun deberia_seleccionar_cristalizacion_alta() {
        val vm = BitacoraViewModel(application)

        vm.cristalizacionSeleccionada = "Alto / Helecho marcado"

        assertEquals(
            "Alto / Helecho marcado",
            vm.cristalizacionSeleccionada
        )
    }







    @Test
    fun deberia_mantener_opciones_cristalizacion() {
        val vm = BitacoraViewModel(application)

        assertEquals(
            listOf(
                "Alto / Helecho marcado",
                "Medio",
                "Nulo"
            ),
            vm.opcionesCristalizacion
        )
    }

    @Test
    fun deberia_rechazar_dia_ciclo_decimal() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "1.5"

        var callbackEjecutado = false

        vm.guardarRegistro {
            callbackEjecutado = true
        }

        assertFalse(callbackEjecutado)
        assertEquals("1.5", vm.diaCiclo)
    }

    @Test
    fun deberia_rechazar_dia_ciclo_con_letras_y_numeros() {
        val vm = BitacoraViewModel(application)

        vm.diaCiclo = "12abc"

        var callbackEjecutado = false

        vm.guardarRegistro {
            callbackEjecutado = true
        }

        assertFalse(callbackEjecutado)
        assertEquals("12abc", vm.diaCiclo)
    }
}