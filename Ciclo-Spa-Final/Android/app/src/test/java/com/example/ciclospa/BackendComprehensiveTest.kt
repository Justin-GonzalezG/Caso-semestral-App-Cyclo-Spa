
package com.example.ciclospa.backend

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class BackendComprehensiveTest : StringSpec({

    val contenido = ContenidoDto(
        id = 1,
        titulo = "Ciclo menstrual",
        categoria = "Educación",
        descripcion = "Información educativa",
        publicado = true
    )

    // ==========================================
    // PRUEBAS DE BACKEND REPOSITORY
    // ==========================================

    "Repository debe obtener todos los contenidos" {
        val api = mockk<BackendApiService>()
        val repository = BackendRepository(api)

        coEvery { api.obtenerContenidos() } returns listOf(contenido)

        repository.obtenerContenidos() shouldBe listOf(contenido)

        coVerify(exactly = 1) {
            api.obtenerContenidos()
        }
    }

    "Repository debe obtener contenido por ID" {
        val api = mockk<BackendApiService>()
        val repository = BackendRepository(api)

        coEvery { api.obtenerContenido(1) } returns contenido

        repository.obtenerContenido(1) shouldBe contenido

        coVerify(exactly = 1) {
            api.obtenerContenido(1)
        }
    }

    "Repository debe crear contenido" {
        val api = mockk<BackendApiService>()
        val repository = BackendRepository(api)

        coEvery {
            api.crearContenido(contenido)
        } returns contenido

        repository.crearContenido(contenido) shouldBe contenido

        coVerify(exactly = 1) {
            api.crearContenido(contenido)
        }
    }

    "Repository debe actualizar contenido" {
        val api = mockk<BackendApiService>()
        val repository = BackendRepository(api)

        coEvery {
            api.actualizarContenido(1, contenido)
        } returns contenido

        repository.actualizarContenido(1, contenido) shouldBe contenido

        coVerify(exactly = 1) {
            api.actualizarContenido(1, contenido)
        }
    }

    "Repository debe eliminar contenido" {
        val api = mockk<BackendApiService>()
        val repository = BackendRepository(api)

        coEvery {
            api.eliminarContenido(1)
        } returns Unit

        repository.eliminarContenido(1)

        coVerify(exactly = 1) {
            api.eliminarContenido(1)
        }
    }

    // ==========================================
    // PRUEBAS DE BACKEND VIEWMODEL
    // ==========================================

    "ViewModel debe cargar contenidos correctamente" {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<BackendRepository>()

                coEvery {
                    repository.obtenerContenidos()
                } returns listOf(contenido)

                val viewModel = BackendViewModel(repository)

                advanceUntilIdle()

                viewModel.contenidos.value shouldBe listOf(contenido)
                viewModel.cargando.value shouldBe false
                viewModel.error.value shouldBe null

                coVerify {
                    repository.obtenerContenidos()
                }
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    "ViewModel debe mostrar error cuando falla la carga" {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<BackendRepository>()

                coEvery {
                    repository.obtenerContenidos()
                } throws RuntimeException("Sin conexión")

                val viewModel = BackendViewModel(repository)

                advanceUntilIdle()

                viewModel.contenidos.value shouldBe emptyList()
                viewModel.cargando.value shouldBe false
                viewModel.error.value shouldBe "Sin conexión"
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    "ViewModel debe crear contenido correctamente" {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<BackendRepository>()

                coEvery {
                    repository.obtenerContenidos()
                } returns listOf(contenido)

                coEvery {
                    repository.crearContenido(any())
                } returns contenido

                val viewModel = BackendViewModel(repository)

                advanceUntilIdle()

                viewModel.crearContenido(
                    "Ciclo menstrual",
                    "Educación",
                    "Información educativa",
                    true
                )

                advanceUntilIdle()

                coVerify {
                    repository.crearContenido(
                        match {
                            it.id == 0 &&
                                    it.titulo == "Ciclo menstrual" &&
                                    it.categoria == "Educación" &&
                                    it.descripcion == "Información educativa" &&
                                    it.publicado
                        }
                    )
                }

                viewModel.cargando.value shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    "ViewModel debe actualizar contenido correctamente" {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<BackendRepository>()

                coEvery {
                    repository.obtenerContenidos()
                } returns listOf(contenido)

                coEvery {
                    repository.actualizarContenido(any(), any())
                } returns contenido

                val viewModel = BackendViewModel(repository)

                advanceUntilIdle()

                viewModel.actualizarContenido(
                    1,
                    "Contenido actualizado",
                    "Educación",
                    "Descripción actualizada",
                    true
                )

                advanceUntilIdle()

                coVerify {
                    repository.actualizarContenido(
                        1,
                        match {
                            it.id == 1 &&
                                    it.titulo == "Contenido actualizado"
                        }
                    )
                }

                viewModel.cargando.value shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    "ViewModel debe eliminar contenido correctamente" {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<BackendRepository>()

                coEvery {
                    repository.obtenerContenidos()
                } returns listOf(contenido)

                coEvery {
                    repository.eliminarContenido(1)
                } returns Unit

                val viewModel = BackendViewModel(repository)

                advanceUntilIdle()

                viewModel.eliminarContenido(1)

                advanceUntilIdle()

                coVerify {
                    repository.eliminarContenido(1)
                }

                viewModel.cargando.value shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }
})
