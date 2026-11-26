
package com.example.ciclospa.repository

import com.example.ciclospa.data.remote.ApiService
import com.example.ciclospa.model.Post
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class PostRepositoryTest : StringSpec({

    "deberia obtener todas las publicaciones" {
        val apiService = mockk<ApiService>()
        val repository = PostRepository(apiService)

        val publicaciones = listOf(
            Post(
                userId = 1,
                id = 1,
                title = "Primera publicación",
                body = "Contenido de prueba"
            ),
            Post(
                userId = 2,
                id = 2,
                title = "Segunda publicación",
                body = "Segundo contenido"
            )
        )

        coEvery { apiService.getPosts() } returns publicaciones

        val resultado = repository.obtenerPosts()

        resultado shouldBe publicaciones
    }

    "deberia obtener una publicacion por su ID" {
        val apiService = mockk<ApiService>()
        val repository = PostRepository(apiService)

        val publicacion = Post(
            userId = 1,
            id = 1,
            title = "Publicación de prueba",
            body = "Contenido de prueba"
        )

        coEvery { apiService.getPostById(1) } returns publicacion

        val resultado = repository.obtenerPostPorId(1)

        resultado shouldBe publicacion
    }

    "deberia crear una publicacion correctamente" {
        val apiService = mockk<ApiService>()
        val repository = PostRepository(apiService)

        val nuevaPublicacion = Post(
            userId = 1,
            id = 101,
            title = "Nueva publicación",
            body = "Contenido nuevo"
        )

        coEvery {
            apiService.createPost(nuevaPublicacion)
        } returns nuevaPublicacion

        val resultado = repository.crearPost(nuevaPublicacion)

        resultado shouldBe nuevaPublicacion
    }
})
