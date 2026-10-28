
package com.example.ciclospa.ui.theme

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.viewmodels.RegistroViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    registroViewModel: RegistroViewModel = viewModel(),
    onRegistroExitoso: (String) -> Unit
) {
    val estado by registroViewModel.estado.collectAsState()
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var fotoBitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    val camara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && estado.fotoUri.isNotBlank()) {
            try {
                contexto.contentResolver.openInputStream(
                    android.net.Uri.parse(estado.fotoUri)
                )?.use { entrada ->
                    fotoBitmap = BitmapFactory.decodeStream(entrada)
                }
            } catch (_: Exception) {
                fotoBitmap = null
            }
        }
    }

    val colorBoton by animateColorAsState(
        targetValue = when {
            estado.guardando -> Color.Gray
            estado.guardado -> Color(0xFF2E7D32)
            else -> MaterialTheme.colorScheme.primary
        },
        label = "colorBoton"
    )

    val espacioLogo by animateDpAsState(
        targetValue = if (estado.guardado) 8.dp else 24.dp,
        label = "espacioLogo"
    )

    LaunchedEffect(estado.guardado) {
        if (estado.guardado) {
            snackbar.showSnackbar("Datos guardados correctamente")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(espacioLogo))

            Text("Bienvenida a Ciclo Spa. Registra tus datos.")

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = registroViewModel::onNombreChange,
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = estado.correo,
                onValueChange = registroViewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange =
                        registroViewModel::onAceptarTerminosChange
                )
                Text("Acepto los términos y condiciones")
            }

            estado.errores.aceptaTerminos?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedButton(
                onClick = {
                    try {
                        val uri = registroViewModel.crearFoto()
                        registroViewModel.onFotoTomada(uri.toString())
                        camara.launch(uri)
                    } catch (e: Exception) {
                        scope.launch {
                            snackbar.showSnackbar(
                                "Error al abrir la cámara: ${e.message}"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar fotografía")
            }

            fotoBitmap?.let { bitmap ->
                Spacer(Modifier.height(12.dp))
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Fotografía del registro",
                    modifier = Modifier.size(140.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text("Vista previa", style = MaterialTheme.typography.titleMedium)
            Text("Nombre: ${estado.nombre.ifBlank { "Sin ingresar" }}")
            Text("Correo: ${estado.correo.ifBlank { "Sin ingresar" }}")
            Text(
                "Términos aceptados: ${
                    if (estado.aceptaTerminos) "Sí" else "No"
                }"
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (registroViewModel.validar()) {
                        registroViewModel.guardar()
                    } else {
                        scope.launch {
                            snackbar.showSnackbar(
                                "Revisa los campos del formulario"
                            )
                        }
                    }
                },
                enabled = !estado.guardando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text(
                        if (estado.guardado) "Datos guardados"
                        else "Guardar registro"
                    )
                }
            }

            AnimatedVisibility(visible = estado.guardado) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Datos guardados en el teléfono",
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onRegistroExitoso(estado.nombre.trim())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continuar")
                    }
                }
            }
        }
    }
}
