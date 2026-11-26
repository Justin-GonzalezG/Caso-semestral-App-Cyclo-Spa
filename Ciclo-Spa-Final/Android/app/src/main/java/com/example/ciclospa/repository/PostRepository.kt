
package com.example.ciclospa.repository

import com.example.ciclospa.data.remote.ApiService
import com.example.ciclospa.model.Post

class PostRepository(
    private val apiService: ApiService
) {

    suspend fun obtenerPosts(): List<Post> {
        return apiService.getPosts()
    }

    suspend fun obtenerPostPorId(id: Int): Post {
        return apiService.getPostById(id)
    }

    suspend fun crearPost(post: Post): Post {
        return apiService.createPost(post)
    }
}
