package com.example.ciclospa.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ciclospa.model.RegistroErrores
import com.example.ciclospa.model.RegistroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistroViewModel : ViewModel() {

    private val _estado = MutableStateFlow(RegistroUiState())

    val estado: StateFlow<RegistroUiState> =
        _estado.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                nombre = valor,
                errores = actual.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                correo = valor,
                errores = actual.errores.copy(correo = null)
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { actual ->
            actual.copy(
                aceptaTerminos = valor,
                errores = actual.errores.copy(
                    aceptaTerminos = null
                )
            )
        }
    }

    fun validar(): Boolean {
        val actual = _estado.value

        val errores = RegistroErrores(
            nombre = if (actual.nombre.isBlank()) {
                "El nombre es obligatorio"
            } else {
                null
            },

            correo = when {
                actual.correo.isBlank() ->
                    "El correo es obligatorio"

                !actual.correo.contains("@") ->
                    "El correo debe incluir @"

                else -> null
            },

            aceptaTerminos = if (!actual.aceptaTerminos) {
                "Debes aceptar los términos"
            } else {
                null
            }
        )

        _estado.update { actual ->
            actual.copy(errores = errores)
        }

        return errores.nombre == null &&
                errores.correo == null &&
                errores.aceptaTerminos == null
    }
}