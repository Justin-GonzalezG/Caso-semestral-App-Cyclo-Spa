
package com.example.ciclospa.viewmodels

import com.example.ciclospa.model.Post
import com.example.ciclospa.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "deberia cargar las publicaciones correctamente" {
        runTest {
            // ARRANGE
            val repository = mockk<PostRepository>()

            val publicaciones = listOf(
                Post(
                    userId = 1,
                    id = 1,
                    title = "Primera publicación",
                    body = "Contenido de prueba"
                ),
                Post(
                    userId = 1,
                    id = 2,
                    title = "Segunda publicación",
                    body = "Segundo contenido"
                )
            )

            coEvery {
                repository.obtenerPosts()
            } returns publicaciones

            val viewModel = PostViewModel(repository)

            // ACT
            viewModel.cargarPosts()
            advanceUntilIdle()

            // ASSERT
            viewModel.posts.value shouldBe publicaciones
            viewModel.cargando.value shouldBe false
            viewModel.error.value shouldBe null
        }
    }

    "deberia mostrar error cuando falla la API" {
        runTest {
            // ARRANGE
            val repository = mockk<PostRepository>()

            coEvery {
                repository.obtenerPosts()
            } throws RuntimeException("Sin conexión")

            val viewModel = PostViewModel(repository)

            // ACT
            viewModel.cargarPosts()
            advanceUntilIdle()

            // ASSERT
            viewModel.posts.value shouldBe emptyList()
            viewModel.cargando.value shouldBe false
            viewModel.error.value shouldBe
                    "Error al conectar con la API: Sin conexión"
        }
    }
})
