package com.example.ciclospa.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciclospa.data.remote.RetrofitInstance
import com.example.ciclospa.model.Post
import com.example.ciclospa.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository =
        PostRepository(RetrofitInstance.apiService)
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarPosts() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                _posts.value = repository.obtenerPosts()
            } catch (e: Exception) {
                _error.value = "Error al conectar con la API: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }
}