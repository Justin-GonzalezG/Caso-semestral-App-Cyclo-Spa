
package com.example.ciclospa.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitacora_tabla")
data class RegistroBitacora(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val fecha: String,

    val diaCiclo: Int,

    val cristalizacion: String,

    val notas: String,

    val fotoUri: String? = null
)
