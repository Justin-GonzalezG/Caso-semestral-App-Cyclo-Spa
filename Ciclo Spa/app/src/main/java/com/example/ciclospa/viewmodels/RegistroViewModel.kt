
package com.example.ciclospa.viewmodels

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciclospa.data.RegistroPreferences
import com.example.ciclospa.model.RegistroErrores
import com.example.ciclospa.model.RegistroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import java.io.OutputStream

class RegistroViewModel(application: Application) :
    AndroidViewModel(application) {

    private val preferencias = RegistroPreferences(application)

    private val _estado = MutableStateFlow(RegistroUiState())
    val estado: StateFlow<RegistroUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val (nombre, correo) = preferencias.leer()
                _estado.update {
                    it.copy(nombre = nombre, correo = correo)
                }
            } catch (e: Exception) {
                // El formulario permanece disponible si falla la lectura.
            }
        }
    }
    fun guardarFotoEnGaleria(uri: Uri): Boolean {
        val app = getApplication<Application>()
        val resolver = app.contentResolver

        val valores = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "CicloSpa_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/CicloSpa"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val destino = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            valores
        ) ?: return false

        return try {
            val entrada = resolver.openInputStream(uri)
                ?: throw IllegalStateException("No se pudo leer la foto")

            entrada.use { origen ->
                val salida: OutputStream = resolver.openOutputStream(destino)
                    ?: throw IllegalStateException("No se pudo guardar la foto")

                salida.use { destinoStream ->
                    origen.copyTo(destinoStream)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val finalizacion = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
                resolver.update(destino, finalizacion, null, null)
            }

            true
        } catch (e: Exception) {
            resolver.delete(destino, null, null)
            false
        }
    }
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                guardado = false,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                guardado = false,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(
                aceptaTerminos = valor,
                errores = it.errores.copy(aceptaTerminos = null)
            )
        }
    }

    fun validar(): Boolean {
        val actual = _estado.value

        val errores = RegistroErrores(
            nombre = if (actual.nombre.isBlank()) {
                "El nombre es obligatorio"
            } else null,
            correo = when {
                actual.correo.isBlank() -> "El correo es obligatorio"
                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(actual.correo.trim()).matches() ->
                    "Ingresa un correo válido"
                else -> null
            },
            aceptaTerminos = if (!actual.aceptaTerminos) {
                "Debes aceptar los términos"
            } else null
        )

        _estado.update { it.copy(errores = errores) }

        return errores.nombre == null &&
                errores.correo == null &&
                errores.aceptaTerminos == null
    }

    fun guardar() {
        if (!validar()) return

        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, guardado = false) }

            try {
                delay(500)
                val actual = _estado.value

                preferencias.guardar(
                    actual.nombre.trim(),
                    actual.correo.trim()
                )

                _estado.update {
                    it.copy(guardando = false, guardado = true)
                }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        guardando = false,
                        guardado = false
                    )
                }
            }
        }
    }

    fun crearFoto(): Uri {
        val aplicacion = getApplication<Application>()
        val directorio = File(aplicacion.cacheDir, "fotos")

        if (!directorio.exists() && !directorio.mkdirs()) {
            throw IllegalStateException("No se pudo crear el directorio de fotografías")
        }

        val archivo = File(
            directorio,
            "foto-${System.currentTimeMillis()}.jpg"
        )

        if (!archivo.createNewFile()) {
            throw IllegalStateException("No se pudo crear el archivo de fotografía")
        }

        return FileProvider.getUriForFile(
            aplicacion,
            "${aplicacion.packageName}.fileprovider",
            archivo
        )
    }

    fun onFotoTomada(uri: String) {
        _estado.update { it.copy(fotoUri = uri) }
    }
}
