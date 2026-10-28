package com.example.ciclospa.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciclospa.data.CicloSpaDatabase
import com.example.ciclospa.model.RegistroBitacora
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BitacoraViewModel(application: Application) : AndroidViewModel(application) {

    private val bitacoraDao = CicloSpaDatabase.obtenerInstancia(application).bitacoraDao()

    val registros: StateFlow<List<RegistroBitacora>> = bitacoraDao.obtenerTodosLosRegistros()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var diaCiclo by mutableStateOf("")
    var cristalizacionSeleccionada by mutableStateOf("Alto / Helecho marcado")
    var notas by mutableStateOf("")
    var fotoUri by mutableStateOf<String?>(null)

    val opcionesCristalizacion = listOf(
        "Alto / Helecho marcado",
        "Medio",
        "Nulo"
    )

    fun guardarRegistro(onGuardadoExitoso: () -> Unit) {
        val diaNum = diaCiclo.toIntOrNull() ?: 1
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val nuevoRegistro = RegistroBitacora(
            fecha = fechaActual,
            diaCiclo = diaNum,
            cristalizacion = cristalizacionSeleccionada,
            notas = notas,
            fotoUri = fotoUri
        )

        viewModelScope.launch {
            bitacoraDao.insertarRegistro(nuevoRegistro)
            diaCiclo = ""
            notas = ""
            fotoUri = null
            onGuardadoExitoso()
        }
    }
}