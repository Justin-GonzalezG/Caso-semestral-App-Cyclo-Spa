
package com.example.ciclospa.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.viewmodels.RegistroViewModel
import kotlinx.coroutines.launch

// Paleta Ciclo Spa: rosa coral, ciruela y crema
private val VerdeSpa = Color(0xFFD85F87)
private val VerdeOscuroSpa = Color(0xFF593247)
private val VerdeSuaveSpa = Color(0xFFFCE8EF)
private val CremaSpa = Color(0xFFFFFAF8)
private val TextoSpa = Color(0xFF493943)
private val GrisSpa = Color(0xFF857680)
private val BordeSpa = Color(0xFFEED5DF)
private val BlancoSpa = Color(0xFFFFFFFF)
private val MalvaSpa = Color(0xFF8B6688)
private val VerdeExitoSpa = Color(0xFF68866D)

@Composable
fun RegisterScreen(
    registroViewModel: RegistroViewModel = viewModel(),
    onRegistroExitoso: (String) -> Unit
) {
    val estado by registroViewModel.estado.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val colorBoton by animateColorAsState(
        targetValue = when {
            estado.guardando -> Color.Gray
            estado.guardado -> MalvaSpa
            else -> VerdeSpa
        },
        label = "colorBoton"
    )

    LaunchedEffect(estado.guardado) {
        if (estado.guardado) {
            snackbar.showSnackbar("¡Registro guardado correctamente!")
        }
    }

    Scaffold(
        containerColor = CremaSpa,
        snackbarHost = {
            SnackbarHost(hostState = snackbar)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Identidad visual de Ciclo Spa
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = VerdeSuaveSpa,
                modifier = Modifier.size(76.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Ciclo Spa",
                        tint = VerdeSpa,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "C I C L O  S P A",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = VerdeSpa
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuroSpa,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Un espacio para ti y tu bienestar.",
                fontSize = 17.sp,
                lineHeight = 24.sp,
                color = TextoSpa,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Completa tus datos para comenzar.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = GrisSpa,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))

            // Nombre completo
            Text(
                text = "Nombre completo",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerdeOscuroSpa
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = registroViewModel::onNombreChange,
                placeholder = {
                    Text("Ingresa tu nombre", fontSize = 16.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = VerdeSpa
                    )
                },
                singleLine = true,
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { mensaje ->
                        Text(mensaje, fontSize = 13.sp)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeSpa,
                    unfocusedBorderColor = BordeSpa,
                    focusedLabelColor = VerdeSpa,
                    cursorColor = VerdeSpa,
                    focusedTextColor = TextoSpa,
                    unfocusedTextColor = TextoSpa,
                    focusedContainerColor = BlancoSpa,
                    unfocusedContainerColor = BlancoSpa
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            // Correo electrónico
            Text(
                text = "Correo electrónico",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerdeOscuroSpa
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.correo,
                onValueChange = registroViewModel::onCorreoChange,
                placeholder = {
                    Text("ejemplo@correo.com", fontSize = 16.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = VerdeSpa
                    )
                },
                singleLine = true,
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { mensaje ->
                        Text(mensaje, fontSize = 13.sp)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeSpa,
                    unfocusedBorderColor = BordeSpa,
                    focusedLabelColor = VerdeSpa,
                    cursorColor = VerdeSpa,
                    focusedTextColor = TextoSpa,
                    unfocusedTextColor = TextoSpa,
                    focusedContainerColor = BlancoSpa,
                    unfocusedContainerColor = BlancoSpa
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            // Términos y condiciones
            Surface(
                color = BlancoSpa,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BordeSpa),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 5.dp
                    )
                ) {
                    Checkbox(
                        checked = estado.aceptaTerminos,
                        onCheckedChange =
                            registroViewModel::onAceptarTerminosChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = VerdeSpa,
                            uncheckedColor = GrisSpa
                        )
                    )

                    Text(
                        text = "Acepto los términos y condiciones",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoSpa
                    )
                }
            }

            estado.errores.aceptaTerminos?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 5.dp)
                )
            }

            Spacer(Modifier.height(26.dp))

            // Resumen de la cuenta
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = BlancoSpa,
                border = BorderStroke(1.dp, BordeSpa),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = VerdeSpa,
                            modifier = Modifier.size(26.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Resumen de tu cuenta",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdeOscuroSpa
                        )
                    }

                    HorizontalDivider(color = BordeSpa)

                    Text(
                        text = "NOMBRE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GrisSpa
                    )

                    Text(
                        text = estado.nombre.ifBlank { "Sin ingresar" },
                        fontSize = 16.sp,
                        color = TextoSpa
                    )

                    Text(
                        text = "CORREO ELECTRÓNICO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GrisSpa
                    )

                    Text(
                        text = estado.correo.ifBlank { "Sin ingresar" },
                        fontSize = 16.sp,
                        color = TextoSpa
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (estado.aceptaTerminos) {
                                VerdeExitoSpa
                            } else {
                                GrisSpa
                            },
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = if (estado.aceptaTerminos) {
                                "Términos aceptados"
                            } else {
                                "Pendiente de aceptar términos"
                            },
                            fontSize = 14.sp,
                            color = TextoSpa
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Crear cuenta: desaparece después de guardar
            AnimatedVisibility(visible = !estado.guardado) {
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
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBoton
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    if (estado.guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = BlancoSpa,
                            strokeWidth = 2.dp
                        )

                        Spacer(Modifier.width(10.dp))
                        Text("Guardando...", fontSize = 17.sp)
                    } else {
                        Text(
                            text = "Crear mi cuenta",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Confirmación después del guardado
            AnimatedVisibility(visible = estado.guardado) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Registro exitoso",
                        tint = VerdeExitoSpa,
                        modifier = Modifier.size(38.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "¡Cuenta creada correctamente!",
                        color = VerdeOscuroSpa,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Tus datos se guardaron en el teléfono.",
                        color = GrisSpa,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = {
                            onRegistroExitoso(estado.nombre.trim())
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeOscuroSpa
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Continuar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Ciclo Spa · Tu bienestar, en armonía con tu ciclo.",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = GrisSpa,
                textAlign = TextAlign.Center
            )
        }
    }
}
