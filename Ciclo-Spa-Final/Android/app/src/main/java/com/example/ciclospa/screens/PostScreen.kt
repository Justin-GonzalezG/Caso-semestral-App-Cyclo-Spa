
package com.example.ciclospa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.model.Post
import com.example.ciclospa.viewmodels.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    onVolver: () -> Unit,
    postViewModel: PostViewModel = viewModel()
) {
    val posts by postViewModel.posts.collectAsState()
    val cargando by postViewModel.cargando.collectAsState()
    val error by postViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.cargarPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Publicaciones de la API")
                },
                navigationIcon = {
                    androidx.compose.material3.TextButton(
                        onClick = onVolver
                    ) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Consumo de API REST",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Información obtenida desde JSONPlaceholder.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                cargando -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando publicaciones...")
                    }
                }

                error != null -> {
                    Text(
                        text = error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            postViewModel.cargarPosts()
                        }
                    ) {
                        Text("Reintentar")
                    }
                }

                posts.isEmpty() -> {
                    Text("No hay publicaciones disponibles.")
                }

                else -> {
                    Text(
                        text = "Publicaciones encontradas: ${posts.size}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(posts) { post ->
                            PostCard(post = post)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Publicación #${post.id}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Usuario: ${post.userId}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
