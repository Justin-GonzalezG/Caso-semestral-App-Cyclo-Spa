
package com.example.ciclospa.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RetrofitInstanceTest {

    @Test
    fun deberia_crear_api_service() {
        val servicio = RetrofitInstance.apiService

        assertNotNull(servicio)
    }

    @Test
    fun deberia_mantener_instancia_api_service() {
        val servicio1 = RetrofitInstance.apiService
        val servicio2 = RetrofitInstance.apiService

        assertNotNull(servicio1)
        assertNotNull(servicio2)
    }
}
