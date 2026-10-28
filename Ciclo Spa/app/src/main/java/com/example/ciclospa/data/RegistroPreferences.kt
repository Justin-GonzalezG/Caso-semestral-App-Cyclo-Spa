
package com.example.ciclospa.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.registroDataStore by preferencesDataStore(
    name = "registro"
)

class RegistroPreferences(private val context: Context) {

    private val claveNombre = stringPreferencesKey("nombre")
    private val claveCorreo = stringPreferencesKey("correo")

    suspend fun guardar(nombre: String, correo: String) {
        context.registroDataStore.edit { preferencias ->
            preferencias[claveNombre] = nombre
            preferencias[claveCorreo] = correo
        }
    }

    suspend fun leer(): Pair<String, String> {
        val preferencias = context.registroDataStore.data.first()
        return Pair(
            preferencias[claveNombre] ?: "",
            preferencias[claveCorreo] ?: ""
        )
    }
}
