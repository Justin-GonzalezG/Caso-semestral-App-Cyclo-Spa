
package com.example.ciclospa.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.ciclospa.model.Post
import com.example.ciclospa.viewmodels.PostViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun deberiaMostrarLasPublicaciones() {
        val publicaciones = listOf(
            Post(
                userId = 1,
                id = 1,
                title = "Primera publicación",
                body = "Contenido de prueba"
            )
        )

        val viewModel = mockk<PostViewModel>(relaxed = true)

        every { viewModel.posts } returns MutableStateFlow(publicaciones)
        every { viewModel.cargando } returns MutableStateFlow(false)
        every { viewModel.error } returns MutableStateFlow(null)

        composeTestRule.setContent {
            PostScreen(
                onVolver = {},
                postViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(
            "Publicaciones de la API"
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Primera publicación"
        ).assertIsDisplayed()

        verify { viewModel.cargarPosts() }
    }

    @Test
    fun deberiaMostrarElEstadoDeCarga() {
        val viewModel = mockk<PostViewModel>(relaxed = true)

        every { viewModel.posts } returns MutableStateFlow(emptyList())
        every { viewModel.cargando } returns MutableStateFlow(true)
        every { viewModel.error } returns MutableStateFlow(null)

        composeTestRule.setContent {
            PostScreen(
                onVolver = {},
                postViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(
            "Cargando publicaciones..."
        ).assertIsDisplayed()
    }

    @Test
    fun deberiaMostrarElMensajeDeError() {
        val viewModel = mockk<PostViewModel>(relaxed = true)

        every { viewModel.posts } returns MutableStateFlow(emptyList())
        every { viewModel.cargando } returns MutableStateFlow(false)
        every { viewModel.error } returns MutableStateFlow("Error de conexión")

        composeTestRule.setContent {
            PostScreen(
                onVolver = {},
                postViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(
            "Error de conexión"
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Reintentar"
        ).assertIsDisplayed()
    }
}
