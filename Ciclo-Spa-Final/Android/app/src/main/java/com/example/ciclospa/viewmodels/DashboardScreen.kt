
package com.example.ciclospa.ui.theme

import android.content.pm.PackageManager
import androidx.compose.material.icons.filled.Person
import android.graphics.Bitmap
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.viewmodels.RegistroViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Paleta Ciclo Spa
// Paleta Ciclo Spa
private val FondoDashboard = Color(0xFFFFFAF8)
private val RosaCoral = Color(0xFFE64F83)
private val RosaPastel = Color(0xFFFFDCE9)
private val LavandaPastel = Color(0xFFE1D8FF)
private val LavandaOscura = Color(0xFF6344A5)
private val MelocotonPastel = Color(0xFFFFE4D1)
private val CiruelaOscura = Color(0xFF49263B)
private val TextoDashboard = Color(0xFF382832)
private val TextoSecundario = Color(0xFF5F4C58)
private val BlancoDashboard = Color(0xFFFFFFFF)
private val BordeDashboard = Color(0xFFE8CEDB)
private val VerdeExito = Color(0xFF52805D)

// Nuevos colores para la tarjeta API REST
private val VerdePastel = Color(0xFFDCEFE2)
private val VerdeOscuro = Color(0xFF397653)

@Composable
fun DashboardScreen(
    nombre: String,
    onCerrarSesion: () -> Unit,
    onAbrirBitacora: () -> Unit,
    onAbrirPosts: () -> Unit,
    onAbrirBackendContenidos: () -> Unit,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val contexto = androidx.compose.ui.platform.LocalContext.current

    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uriCamara by remember { mutableStateOf<Uri?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Cargar fotografía seleccionada o tomada
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

    // Galería
    val galeria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) cargarFoto(uri)
    }

    // Cámara
    val camara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val uri = uriCamara

        if (exito && uri != null) {
            cargarFoto(uri)

            val guardada = registroViewModel.guardarFotoEnGaleria(uri)

            if (!guardada) {
                mensajeError =
                    "La foto se tomó, pero no se pudo guardar en la galería."
            }
        } else if (!exito) {
            mensajeError = "No se tomó la fotografía."
        }
    }

    // Permiso de cámara
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

    // Fecha actual en español
    val fecha = LocalDate.now().format(
        DateTimeFormatter.ofPattern(
            "EEEE d 'de' MMMM 'de' yyyy",
            Locale("es", "CL")
        )
    ).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale("es", "CL"))
        else it.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoDashboard)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Barra superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCerrarSesion,
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, BordeDashboard
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CiruelaOscura
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Cerrar sesión", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(18.dp))

        // Foto o inicial de usuario
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(LavandaPastel),
            contentAlignment = Alignment.Center
        ) {
            val foto = fotoBitmap

            if (foto != null) {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Text(
                    text = nombre.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = LavandaOscura
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "¡Bienvenida, $nombre!",
            fontSize = 25.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CiruelaOscura,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(7.dp))

        Text(
            text = "Nos alegra tenerte en Ciclo Spa.",
            fontSize = 16.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(26.dp))

        // Tarjeta Tu día: rosa pastel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdePastel
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Today,
                        contentDescription = null,
                        tint = RosaCoral,
                        modifier = Modifier.size(27.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "Tu día",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = CiruelaOscura
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = fecha,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoDashboard
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Registra tus datos del ciclo para consultar " +
                            "la información correspondiente a tu día.",
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = TextoDashboard
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // Tarjeta Mi bitácora: lavanda
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = LavandaPastel
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = null,
                    tint = LavandaOscura,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Mi bitácora",
                    fontSize = 25.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LavandaOscura
                )

                Spacer(Modifier.height(9.dp))

                Text(
                    text = "Registra el día de tu ciclo, la cristalización y tus observaciones.",
                    fontSize = 17.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoDashboard,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onAbrirBitacora,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavandaOscura
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Abrir mi bitácora",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))


// Tarjeta de consulta API REST
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdePastel
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Conexión API REST",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CiruelaOscura
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Consulta publicaciones obtenidas desde un servicio externo mediante Retrofit.",
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = TextoDashboard,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onAbrirPosts,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosaCoral
                    )
                ) {
                    Text(
                        text = "Consultar API REST",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))


        Spacer(Modifier.height(18.dp))

// Tarjeta de conexión con microservicio Spring Boot
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = RosaPastel
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Microservicio Spring Boot",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CiruelaOscura
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Consulta contenidos educativos almacenados en nuestra propia API desarrollada con Spring Boot.",
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = TextoDashboard,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onAbrirBackendContenidos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavandaOscura
                    )
                ) {
                    Text(
                        text = "Consultar microservicio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Tarjeta Tu perfil: melocotón
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MelocotonPastel
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = RosaCoral,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Tu perfil",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = CiruelaOscura
                )

                Spacer(Modifier.height(9.dp))

                Text(
                    text = "Personaliza tu perfil con una fotografía.",
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = TextoDashboard,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                // Seleccionar foto de galería
                Button(
                    onClick = {
                        galeria.launch("image/*")
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RosaCoral
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Elegir foto de la galería",
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Tomar fotografía con cámara
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
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, RosaCoral
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CiruelaOscura
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Tomar foto con la cámara",
                        fontSize = 15.sp
                    )
                }

                mensajeError?.let { mensaje ->
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Ciclo Spa · Un momento para cuidar de ti ♡",
            fontSize = 13.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))
    }
}
