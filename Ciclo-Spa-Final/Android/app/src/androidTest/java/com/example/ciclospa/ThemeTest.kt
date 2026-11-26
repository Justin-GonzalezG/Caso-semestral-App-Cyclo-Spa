package com.example.ciclospa

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.sp
import com.example.ciclospa.ui.theme.CicloSpaTheme
import com.example.ciclospa.ui.theme.Purple40
import com.example.ciclospa.ui.theme.Purple80
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ThemeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun temaClaroDebeAplicarColoresCorrectos() {
        var colorPrimario = Purple80

        composeTestRule.setContent {
            CicloSpaTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                colorPrimario = MaterialTheme.colorScheme.primary
                Text("Tema claro Ciclo Spa")
            }
        }

        assertEquals(Purple40, colorPrimario)

        composeTestRule
            .onNodeWithText("Tema claro Ciclo Spa")
            .assertIsDisplayed()
    }

    @Test
    fun temaOscuroDebeAplicarColoresCorrectos() {
        var colorPrimario = Purple40

        composeTestRule.setContent {
            CicloSpaTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                colorPrimario = MaterialTheme.colorScheme.primary
                Text("Tema oscuro Ciclo Spa")
            }
        }

        assertEquals(Purple80, colorPrimario)

        composeTestRule
            .onNodeWithText("Tema oscuro Ciclo Spa")
            .assertIsDisplayed()
    }

    @Test
    fun temaDebeAplicarTipografiaCorrecta() {
        var tamanioFuente = 0.sp

        composeTestRule.setContent {
            CicloSpaTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                tamanioFuente =
                    MaterialTheme.typography.bodyLarge.fontSize

                Text(
                    text = "Prueba de tipografia",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        assertEquals(16.sp, tamanioFuente)

        composeTestRule
            .onNodeWithText("Prueba de tipografia")
            .assertIsDisplayed()
    }
}
