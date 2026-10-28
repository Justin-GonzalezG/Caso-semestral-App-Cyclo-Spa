package com.example.ciclospa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ciclospa.model.RegistroBitacora

@Database(
    entities = [Usuario::class, RegistroBitacora::class],
    version = 2,
    exportSchema = false
)
abstract class CicloSpaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun bitacoraDao(): BitacoraDao

    companion object {
        @Volatile
        private var INSTANCE: CicloSpaDatabase? = null

        fun obtenerInstancia(context: Context): CicloSpaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CicloSpaDatabase::class.java,
                    "ciclo_spa_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}