package com.example.ciclospa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ciclospa.model.RegistroBitacora
import kotlinx.coroutines.flow.Flow

@Dao
interface BitacoraDao {

    @Query("SELECT * FROM bitacora_tabla ORDER BY fecha DESC")
    fun obtenerTodosLosRegistros(): Flow<List<RegistroBitacora>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRegistro(registro: RegistroBitacora)
}