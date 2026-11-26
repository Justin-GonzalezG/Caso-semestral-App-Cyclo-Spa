
package com.example.ciclospa.ui.theme

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

import org.junit.Rule

class LoginScreenTest {

    @get:Rule
    val regla = createComposeRule()

    @Test
    fun deberia_mostrar_elementos_login() {
        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, responder ->
                    responder(false, "Error")
                },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        regla.onNodeWithText("Nombre o correo electrónico").assertIsDisplayed()
        regla.onNodeWithText("Entrar").assertIsDisplayed()
        regla.onNodeWithText("Volver").assertIsDisplayed()
    }

    @Test
    fun deberia_mostrar_error_si_identificador_vacio() {
        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, _ -> },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Entrar").performClick()

        regla.onNodeWithText(
            "Ingresa tu nombre o correo."
        ).assertIsDisplayed()
    }

    @Test
    fun deberia_enviar_identificador_correctamente() {
        var identificadorRecibido = ""

        regla.setContent {
            LoginScreen(
                onIniciarSesion = { identificador, responder ->
                    identificadorRecibido = identificador
                    responder(true, "")
                },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Nombre o correo electrónico")
            .performTextInput("medlin@gmail.com")

        regla.onNodeWithText("Entrar").performClick()

        regla.runOnIdle {
            assertEquals(
                "medlin@gmail.com",
                identificadorRecibido
            )
        }
    }

    @Test
    fun deberia_limpiar_error_al_escribir() {
        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, _ -> },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Entrar").performClick()

        regla.onNodeWithText(
            "Ingresa tu nombre o correo."
        ).assertIsDisplayed()

        regla.onNodeWithText("Nombre o correo electrónico")
            .performTextInput("Medlin")

        regla.onNodeWithText(
            "Ingresa tu nombre o correo."
        ).assertDoesNotExist()
    }

    @Test
    fun deberia_mostrar_error_si_inicio_sesion_falla() {
        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, responder ->
                    responder(
                        false,
                        "Nombre o correo incorrecto."
                    )
                },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Nombre o correo electrónico")
            .performTextInput("Medlin")

        regla.onNodeWithText("Entrar").performClick()

        regla.onNodeWithText(
            "Nombre o correo incorrecto."
        ).assertIsDisplayed()
    }

    @Test
    fun deberia_mostrar_verificando_si_respuesta_pendiente() {
        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, _ -> },
                onVolver = {}
            )
        }

        regla.onNodeWithText("Nombre o correo electrónico")
            .performTextInput("Medlin")

        regla.onNodeWithText("Entrar").performClick()

        regla.onNodeWithText("Verificando...")
            .assertIsDisplayed()
    }

    @Test
    fun deberia_ejecutar_accion_volver() {
        var volverEjecutado = false

        regla.setContent {
            LoginScreen(
                onIniciarSesion = { _, responder ->
                    responder(false, "Error")
                },
                onVolver = {
                    volverEjecutado = true
                }
            )
        }

        regla.onNodeWithText("Volver").performClick()

        regla.runOnIdle {
            assertTrue(volverEjecutado)
        }
    }
}
