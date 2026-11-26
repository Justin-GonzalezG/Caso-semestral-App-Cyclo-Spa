
package com.example.ciclospa.viewmodels

import com.example.ciclospa.navigation.NavigationEvent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : StringSpec({

    "Debe emitir evento PopBackStack" {
        runTest {
            Dispatchers.setMain(
                StandardTestDispatcher(testScheduler)
            )

            try {
                val viewModel = MainViewModel()

                // Solicitar volver atrás
                viewModel.popBackStack()

                // Ejecutar las corrutinas pendientes
                advanceUntilIdle()

                // Recibir el evento
                val evento = viewModel.navigationEvents.first()

                evento shouldBe NavigationEvent.PopBackStack

            } finally {
                Dispatchers.resetMain()
            }
        }
    }
})
