package com.example.ciclospa.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Para emulador Android: 10.0.2.2 apunta al computador anfitrión. */
object CicloSpaBackend {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    val api: ContenidoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContenidoApiService::class.java)
    }
}
