package com.example.ciclospa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class],
    version = 1,
    exportSchema = false
)
abstract class CicloSpaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: CicloSpaDatabase? = null

        fun obtenerInstancia(context: Context): CicloSpaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CicloSpaDatabase::class.java,
                    "ciclo_spa_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}