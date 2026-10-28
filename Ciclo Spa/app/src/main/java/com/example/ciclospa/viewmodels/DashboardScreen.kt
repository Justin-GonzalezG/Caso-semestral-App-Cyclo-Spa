
package com.example.ciclospa.ui.theme

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.viewmodels.RegistroViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardScreen(
    nombre: String,
    onCerrarSesion: () -> Unit,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val contexto = LocalContext.current

    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uriCamara by remember { mutableStateOf<Uri?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Cargar la fotografía seleccionada o tomada.
    fun cargarFoto(uri: Uri) {
        try {
            val bitmap = contexto.contentResolver
                .openInputStream(uri)
                ?.use { entrada -> BitmapFactory.decodeStream(entrada) }

            if (bitmap != null) {
                fotoBitmap = bitmap
                mensajeError = null
            } else {
                mensajeError = "No se pudo cargar la fotografía."
            }
        } catch (e: Exception) {
            mensajeError = "No se pudo abrir la fotografía."
        }
    }

    // Seleccionar una imagen de la galería.
    val galeria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            cargarFoto(uri)
        }
    }

    // Recibir el resultado de la cámara.
    val camara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val uri = uriCamara

        if (exito && uri != null) {
            cargarFoto(uri)

            val guardada = registroViewModel.guardarFotoEnGaleria(uri)

            if (!guardada) {
                mensajeError = "La foto se tomó, pero no se pudo guardar en la galería."
            }
        } else if (!exito) {
            mensajeError = "No se tomó la fotografía."
        }
    }
    // Solicitar permiso de cámara cuando sea necesario.
    val permisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            try {
                val uri = registroViewModel.crearFoto()
                uriCamara = uri
                camara.launch(uri)
            } catch (e: Exception) {
                mensajeError = "Error al abrir la cámara: ${e.message}"
            }
        } else {
            mensajeError = "Debes conceder permiso para usar la cámara."
        }
    }

    // Fecha actual en español.
    val fecha = LocalDate.now().format(
        DateTimeFormatter.ofPattern(
            "EEEE d 'de' MMMM 'de' yyyy",
            Locale("es", "CL")
        )
    ).replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale("es", "CL"))
        } else {
            it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para cerrar sesión.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = onCerrarSesion) {
                Text("Cerrar sesión")
            }
        }

        Spacer(Modifier.height(20.dp))

        // Fotografía o inicial del usuario.
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val foto = fotoBitmap

            if (foto != null) {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = nombre.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "¡Bienvenida, $nombre!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text("Nos alegra tenerte en Ciclo Spa.")

        Spacer(Modifier.height(24.dp))

        // Tarjeta del día.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "Tu día",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(fecha)

                Spacer(Modifier.height(12.dp))

                Text(
                    "Registra tus datos del ciclo para consultar " +
                            "la información correspondiente a tu día."
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Tarjeta del perfil.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu perfil",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text("Personaliza tu perfil con una fotografía.")

                Spacer(Modifier.height(12.dp))

                // Abrir la galería.
                Button(
                    onClick = {
                        galeria.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Elegir foto de la galería")
                }

                // Abrir la cámara directamente si el permiso está concedido.
                OutlinedButton(
                    onClick = {
                        val concedido =
                            ContextCompat.checkSelfPermission(
                                contexto,
                                android.Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED

                        if (concedido) {
                            try {
                                val uri = registroViewModel.crearFoto()
                                uriCamara = uri
                                camara.launch(uri)
                            } catch (e: Exception) {
                                mensajeError =
                                    "Error al abrir la cámara: ${e.message}"
                            }
                        } else {
                            permisoCamara.launch(
                                android.Manifest.permission.CAMERA
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tomar foto con la cámara")
                }

                // Mostrar posibles errores.
                mensajeError?.let { mensaje ->
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
