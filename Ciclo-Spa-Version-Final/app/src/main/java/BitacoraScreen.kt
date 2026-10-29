package com.example.ciclospa.ui.theme

import android.content.Intent

import android.net.Uri
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.app.Application
import android.content.ContentValues
import android.provider.MediaStore
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ciclospa.model.RegistroBitacora
import com.example.ciclospa.viewmodels.BitacoraViewModel


private val Rosa = Color(0xFFC7657D)
private val RosaClaro = Color(0xFFF8E6E9)
private val Morado = Color(0xFF754657)
private val Lila = Color(0xFFF1EAEC)
private val Turquesa = Color(0xFF7E907A)
private val Fondo = Color(0xFFFCF8F6)
private val TextoOscuro = Color(0xFF30242C)




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraScreen(
    onVolver: () -> Unit,
    bitacoraViewModel: BitacoraViewModel = viewModel()
) {
    val registros by bitacoraViewModel.registros.collectAsState()
    val context = LocalContext.current

    var mensaje by remember { mutableStateOf("") }

    var uriFotoCamara by remember {
        mutableStateOf<Uri?>(null)
    }

    val camara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriFotoCamara?.let { uri ->
                bitacoraViewModel.fotoUri = uri.toString()
                mensaje = "Fotografía capturada correctamente."
            }
        } else {
            mensaje = "No se pudo tomar la fotografía."
        }
    }

// FUNCIÓN PARA ABRIR LA CÁMARA
    val abrirCamara: () -> Unit = {
        try {
            val valores = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "ciclospa_${System.currentTimeMillis()}.jpg"
                )
                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/jpeg"
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                valores
            )

            if (uri != null) {
                uriFotoCamara = uri
                camara.launch(uri)
            } else {
                mensaje = "No se pudo preparar la fotografía."
            }
        } catch (e: Exception) {
            mensaje = "Error al abrir la cámara: ${e.message}"
        }
    }

// SOLICITAR PERMISO DE CÁMARA
    val permisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara()
        } else {
            mensaje = "Debes conceder permiso para usar la cámara."
        }
    }

    val estadoLista = rememberLazyListState()
    val alcance = rememberCoroutineScope()


    fun irAlHistorial() {
        alcance.launch {
            estadoLista.animateScrollToItem(3)
        }
    }
    var registroSeleccionado by remember {
        mutableStateOf<RegistroBitacora?>(null)
    }
    var mostrarConfirmacionEliminar by remember {
        mutableStateOf(false)
    }
    var mostrarGaleria by remember { mutableStateOf(false) }

    val galeria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                bitacoraViewModel.getApplication<Application>()
                    .contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
            } catch (_: Exception) {
                // Algunos proveedores no permiten conservar el permiso.
            }
            bitacoraViewModel.fotoUri = it.toString()
        }
    }

    Scaffold(
        containerColor = Fondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Mi Bitácora",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Tu espacio de bienestar",
                            fontSize = 25.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Morado
                )
            )
        }
    ) { padding ->
        LazyColumn(
            state = estadoLista,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 18.dp,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {


            item {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically { -it / 3 }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFFF7E3E6),
                                        Color(0xFFEEDCDF)
                                    )
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .padding(22.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(42.dp)
                            )
                            Text(
                                text = "¡Hola! 🌸",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoOscuro
                            )
                            Text(
                                text = "Registra cómo te sientes y guarda tus recuerdos de cada día.",
                                fontSize = 18.sp,
                                color = TextoOscuro,
                                lineHeight = 25.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.22f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        "  ${registros.size} registros guardados  ",
                                        modifier = Modifier.padding(8.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Turquesa
                                    )
                                }
                            }
                            Button(
                                onClick = { irAlHistorial() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 58.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Morado
                                )
                            ) {
                                Icon(
                                    Icons.Default.History,
                                    contentDescription = null
                                )

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    "Ver mis registros (${registros.size})",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            item {
                TituloSeccion(
                    icono = "📝",
                    titulo = "Nuevo registro",
                    subtitulo = "Completa los datos de tu día"
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Conserva aquí todos los campos que ya tienes.
                        Text(
                            "¿Qué día de tu ciclo es?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )

                        OutlinedTextField(
                            value = bitacoraViewModel.diaCiclo,
                            onValueChange = {
                                if (it.all(Char::isDigit)) {
                                    bitacoraViewModel.diaCiclo = it
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Día del ciclo", fontSize = 17.sp) },
                            placeholder = { Text("Ejemplo: 5", fontSize = 17.sp) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Rosa
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        Text(
                            "Nivel de cristalización",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )

                        bitacoraViewModel.opcionesCristalizacion.forEachIndexed { index, opcion ->
                            val colores = listOf(Rosa, Morado, Turquesa)
                            val fondos = listOf(RosaClaro, Lila, Color(0xFFD9F8F3))
                            val seleccionado =
                                bitacoraViewModel.cristalizacionSeleccionada == opcion

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        bitacoraViewModel.cristalizacionSeleccionada = opcion
                                    },
                                shape = RoundedCornerShape(17.dp),
                                color = if (seleccionado) fondos[index]
                                else Color(0xFFF8F5FA),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (seleccionado) 2.dp else 1.dp,
                                    color = if (seleccionado) colores[index]
                                    else Color(0xFFE5DFE9)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = seleccionado,
                                        onClick = {
                                            bitacoraViewModel.cristalizacionSeleccionada = opcion
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = colores[index]
                                        )
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            opcion,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextoOscuro
                                        )
                                        Text(
                                            when (index) {
                                                0 -> "Helecho marcado"
                                                1 -> "Presencia intermedia"
                                                else -> "Sin cristalización observada"
                                            },
                                            fontSize = 15.sp,
                                            color = Color(0xFF74667D)
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            "Notas de tu día",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )

                        OutlinedTextField(
                            value = bitacoraViewModel.notas,
                            onValueChange = { bitacoraViewModel.notas = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp),
                            placeholder = {
                                Text(
                                    "¿Cómo te sientes hoy? Escribe aquí...",
                                    fontSize = 17.sp
                                )
                            },
                            minLines = 3,
                            shape = RoundedCornerShape(18.dp)
                        )

                        Text(
                            "Fotografía (opcional)",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { galeria.launch(arrayOf("image/*")) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 58.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Rosa
                                )
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null)
                                Spacer(Modifier.width(7.dp))
                                Text("Galería", fontSize = 17.sp)
                            }

                            Button(
                                onClick = {
                                    val permisoConcedido = ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (permisoConcedido) {
                                        abrirCamara()
                                    } else {
                                        permisoCamara.launch(
                                            android.Manifest.permission.CAMERA
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 58.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Morado
                                )
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(7.dp))
                                Text("Cámara", fontSize = 17.sp)
                            }
                        }




                        bitacoraViewModel.fotoUri?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Fotografía seleccionada",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .background(RosaClaro, RoundedCornerShape(20.dp))
                            )
                            TextButton(
                                onClick = { bitacoraViewModel.fotoUri = null }
                            ) {
                                Text("Quitar fotografía", fontSize = 16.sp)
                            }
                        }

                        Button(
                            onClick = {
                                val dia = bitacoraViewModel.diaCiclo.toIntOrNull()
                                if (dia == null || dia < 1) {
                                    mensaje = "Escribe un día del ciclo válido, por ejemplo 1."
                                } else {
                                    bitacoraViewModel.guardarRegistro {
                                        mensaje = "¡Registro guardado correctamente! 💖"
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 64.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Turquesa
                            )
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Guardar mi registro",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        if (mensaje.isNotBlank()) {
                            Text(
                                mensaje,
                                color = if (mensaje.startsWith("¡")) Turquesa else Rosa,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                TituloSeccion(
                    icono = "💖",
                    titulo = "Mi historial",
                    subtitulo = "Toca un registro para ver todos sus detalles"
                )
            }


            if (registros.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Lila
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = null,
                                tint = Morado,
                                modifier = Modifier.size(52.dp)
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                "Tu historia comienza aquí",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoOscuro
                            )

                            Text(
                                "Cuando guardes tu primer registro, aparecerá en esta sección.",
                                fontSize = 17.sp,
                                color = TextoOscuro
                            )
                        }
                    }
                }
            } else {
                items(registros, key = { it.id }) { registro ->
                    TarjetaRegistro(
                        registro = registro,
                        onClick = {
                            registroSeleccionado = registro
                        },
                        onEliminar = {
                            registroSeleccionado = registro
                            mostrarConfirmacionEliminar = true
                        }
                    )
                }
            }



        } // Fin de LazyColumn

        // Diálogo para consultar los detalles de un registro.
        registroSeleccionado?.takeUnless { mostrarConfirmacionEliminar }?.let { registro ->
            AlertDialog(
                onDismissRequest = { registroSeleccionado = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(28.dp),
                title = {
                    Text(
                        text = "🌷 Detalle de tu registro",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Morado
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 450.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Detalle("📅 Fecha", registro.fecha)
                        Detalle("🌸 Día del ciclo", registro.diaCiclo.toString())
                        Detalle("✨ Cristalización", registro.cristalizacion)
                        Detalle(
                            "📝 Notas",
                            registro.notas.ifBlank { "Sin notas registradas" }
                        )

                        registro.fotoUri?.let { uri ->
                            Text(
                                "📷 Fotografía",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Morado
                            )
                            AsyncImage(
                                model = uri,
                                contentDescription = "Fotografía del registro",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { registroSeleccionado = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Morado),
                        modifier = Modifier.heightIn(min = 50.dp)
                    ) {
                        Text("Cerrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarConfirmacionEliminar = true
                        }
                    ) {
                        Text("Eliminar registro", color = Rosa)
                    }
                }
            )
        }

        // Confirmación antes de eliminar.
        if (mostrarConfirmacionEliminar && registroSeleccionado != null) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacionEliminar = false },
                title = { Text("¿Eliminar registro?") },
                text = { Text("Esta acción no se puede deshacer.") },

                confirmButton = {
                    TextButton(
                        onClick = {
                            val registro = registroSeleccionado

                            if (registro != null) {
                                bitacoraViewModel.eliminarRegistro(
                                    registro,
                                    onEliminado = {
                                        mostrarConfirmacionEliminar = false
                                        registroSeleccionado = null
                                    }
                                )
                            } else {
                                mostrarConfirmacionEliminar = false
                            }
                        }
                    ) {
                        Text("Eliminar", color = Rosa)
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = { mostrarConfirmacionEliminar = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun TituloSeccion(
    icono: String,
    titulo: String,
    subtitulo: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$icono  $titulo",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Morado
        )

        Text(
            text = subtitulo,
            fontSize = 16.sp,
            color = Color(0xFF75667E)
        )
    }
}

@Composable
private fun TarjetaRegistro(
    registro: RegistroBitacora,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 3 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(23.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (registro.cristalizacion) {
                    "Alto / Helecho marcado" -> RosaClaro
                    "Medio" -> Lila
                    else -> Color(0xFFD9F8F3)
                }
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = when (registro.cristalizacion) {
                        "Alto / Helecho marcado" -> Rosa
                        "Medio" -> Morado
                        else -> Turquesa
                    },
                    modifier = Modifier.size(62.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            registro.diaCiclo.toString(),
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.width(15.dp))


                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Día ${registro.diaCiclo} del ciclo",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextoOscuro,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = registro.fecha,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF62556D),
                        lineHeight = 23.sp
                    )

                    Surface(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = registro.cristalizacion,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Morado
                        )
                    }

                    if (registro.fotoUri != null) {
                        Text(
                            text = "📷 Fotografía guardada",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro
                        )
                    }
                }

                Text(
                    text = "›",
                    fontSize = 38.sp,
                    color = Morado,
                    fontWeight = FontWeight.ExtraBold
                )

                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar registro",
                        tint = Rosa
                    )
                }
            }
        }
    }
}


@Composable
private fun Detalle(
    titulo: String,
    valor: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Morado
        )
        Text(
            valor,
            fontSize = 17.sp,
            color = TextoOscuro
        )
    }
}
