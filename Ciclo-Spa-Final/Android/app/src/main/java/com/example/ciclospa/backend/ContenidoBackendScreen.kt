
package com.example.ciclospa.backend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val FondoCRUD = Color(0xFFFFFAF8)
private val RosaCRUD = Color(0xFFE64F83)
private val RosaPastelCRUD = Color(0xFFFFDCE9)
private val LavandaCRUD = Color(0xFFE1D8FF)
private val LavandaOscuraCRUD = Color(0xFF6344A5)
private val CiruelaCRUD = Color(0xFF49263B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoBackendScreen(
    onVolver: () -> Unit,
    backendViewModel: BackendViewModel = viewModel()
) {
    val contenidos = backendViewModel.contenidos.value
    val cargando = backendViewModel.cargando.value
    val error = backendViewModel.error.value

    var mostrarFormulario by remember { mutableStateOf(false) }
    var contenidoEditar by remember { mutableStateOf<ContenidoDto?>(null) }
    var contenidoEliminar by remember { mutableStateOf<ContenidoDto?>(null) }

    Scaffold(
        containerColor = FondoCRUD,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Contenidos educativos",
                        fontWeight = FontWeight.Bold,
                        color = CiruelaCRUD
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver", color = LavandaOscuraCRUD)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoCRUD
                )
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
                "Administra la biblioteca educativa de Ciclo Spa.",
                color = CiruelaCRUD,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = {
                    contenidoEditar = null
                    mostrarFormulario = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RosaCRUD
                )
            ) {
                Text(
                    "+ Agregar contenido",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { backendViewModel.cargarContenidos() },
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar contenidos")
            }

            if (cargando) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = RosaCRUD
                )
            }

            error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            if (contenidos.isEmpty() && !cargando) {
                Text(
                    "No hay contenidos disponibles.",
                    color = CiruelaCRUD
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(
                    items = contenidos,
                    key = { it.id }
                ) { contenido ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LavandaCRUD
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                contenido.titulo,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = CiruelaCRUD
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                contenido.categoria,
                                color = LavandaOscuraCRUD,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                contenido.descripcion,
                                color = CiruelaCRUD,
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                if (contenido.publicado)
                                    "✓ Publicado"
                                else
                                    "No publicado",
                                color = CiruelaCRUD,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        contenidoEditar = contenido
                                        mostrarFormulario = true
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LavandaOscuraCRUD
                                    )
                                ) {
                                    Text("Editar")
                                }

                                Button(
                                    onClick = {
                                        contenidoEliminar = contenido
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RosaCRUD
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Formulario para crear y editar
    if (mostrarFormulario) {
        FormularioContenido(
            contenido = contenidoEditar,
            onCerrar = {
                mostrarFormulario = false
                contenidoEditar = null
            },
            onGuardar = { titulo, categoria, descripcion, publicado ->
                val existente = contenidoEditar

                if (existente == null) {
                    backendViewModel.crearContenido(
                        titulo,
                        categoria,
                        descripcion,
                        publicado
                    )
                } else {
                    backendViewModel.actualizarContenido(
                        existente.id,
                        titulo,
                        categoria,
                        descripcion,
                        publicado
                    )
                }

                mostrarFormulario = false
                contenidoEditar = null
            }
        )
    }

    // Confirmación de eliminación
    contenidoEliminar?.let { contenido ->
        AlertDialog(
            onDismissRequest = {
                contenidoEliminar = null
            },
            title = {
                Text("Eliminar contenido")
            },
            text = {
                Text("¿Deseas eliminar '${contenido.titulo}'?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        backendViewModel.eliminarContenido(contenido.id)
                        contenidoEliminar = null
                    }
                ) {
                    Text("Eliminar", color = RosaCRUD)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        contenidoEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun FormularioContenido(
    contenido: ContenidoDto?,
    onCerrar: () -> Unit,
    onGuardar: (String, String, String, Boolean) -> Unit
) {
    var titulo by remember(contenido?.id) {
        mutableStateOf(contenido?.titulo ?: "")
    }

    var categoria by remember(contenido?.id) {
        mutableStateOf(contenido?.categoria ?: "")
    }

    var descripcion by remember(contenido?.id) {
        mutableStateOf(contenido?.descripcion ?: "")
    }

    var publicado by remember(contenido?.id) {
        mutableStateOf(contenido?.publicado ?: true)
    }

    val esEdicion = contenido != null

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(
                if (esEdicion) "Editar contenido"
                else "Nuevo contenido",
                color = CiruelaCRUD,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = publicado,
                        onCheckedChange = { publicado = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = RosaCRUD
                        )
                    )

                    Text("Publicado")
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = titulo.isNotBlank() &&
                        categoria.isNotBlank() &&
                        descripcion.isNotBlank(),
                onClick = {
                    onGuardar(
                        titulo.trim(),
                        categoria.trim(),
                        descripcion.trim(),
                        publicado
                    )
                }
            ) {
                Text("Guardar", color = LavandaOscuraCRUD)
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text("Cancelar")
            }
        },
        containerColor = FondoCRUD
    )
}
