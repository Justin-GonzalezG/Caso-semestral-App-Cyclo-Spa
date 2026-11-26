
package com.example.ciclospa.data.remote

import com.example.ciclospa.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Consultar todas las publicaciones
    @GET("posts")
    suspend fun getPosts(): List<Post>

    // Consultar una publicación por ID
    @GET("posts/{id}")
    suspend fun getPostById(
        @Path("id") postId: Int
    ): Post

    // Enviar una nueva publicación
    @POST("posts")
    suspend fun createPost(
        @Body post: Post
    ): Post
}
