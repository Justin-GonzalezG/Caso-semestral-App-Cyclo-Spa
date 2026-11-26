
package com.example.ciclospa.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import androidx.room.Room

@RunWith(AndroidJUnit4::class)
class DataPackageTest {

    @Test
    fun probarBaseDatosYPreferencias() = runBlocking {

        val context = ApplicationProvider.getApplicationContext<Context>()

        // BASE DE DATOS ROOM EN MEMORIA
        val database = Room.inMemoryDatabaseBuilder(
            context,
            CicloSpaDatabase::class.java
        ).allowMainThreadQueries().build()

        try {

            // 1. Verificar acceso a DAO
            val usuarioDao = database.usuarioDao()

            // 2. Insertar usuarios
            val usuario1 = Usuario(
                nombre = "Medlin",
                correo = "medlin@test.cl"
            )

            val usuario2 = Usuario(
                nombre = "Justin",
                correo = "justin@test.cl"
            )

            usuarioDao.insertar(usuario1)
            usuarioDao.insertar(usuario2)

            // 3. Verificar listado de usuarios
            val lista = usuarioDao.obtenerUsuarios().first()

            assertEquals(2, lista.size)

            // 4. Buscar usuario por correo
            val usuarioEncontrado =
                usuarioDao.buscarPorCorreo("medlin@test.cl")

            assertNotNull(usuarioEncontrado)
            assertEquals("Medlin", usuarioEncontrado?.nombre)

            // 5. Verificar búsqueda inexistente
            val usuarioNoEncontrado =
                usuarioDao.buscarPorCorreo("inexistente@test.cl")

            assertNull(usuarioNoEncontrado)

            // 6. Probar RegistroPreferences
            val preferencias = RegistroPreferences(context)

            preferencias.guardar(
                nombre = "Medlin",
                correo = "medlin@test.cl"
            )

            val datos = preferencias.leer()

            assertEquals("Medlin", datos.first)
            assertEquals("medlin@test.cl", datos.second)

            // 7. Validar por nombre
            assertTrue(
                preferencias.validarUsuario("Medlin")
            )

            // 8. Validar por correo
            assertTrue(
                preferencias.validarUsuario("medlin@test.cl")
            )

            // 9. Validar espacios y mayúsculas
            assertTrue(
                preferencias.validarUsuario(" MEDLIN ")
            )

            assertTrue(
                preferencias.validarUsuario(" MEDLIN@TEST.CL ")
            )

            // 10. Validar usuario incorrecto
            assertFalse(
                preferencias.validarUsuario("usuarioincorrecto")
            )

        } finally {
            database.close()
        }
    }
}
