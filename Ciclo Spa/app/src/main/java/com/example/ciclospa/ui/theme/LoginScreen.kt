
package com.example.ciclospa.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onIniciarSesion: (String, (Boolean, String) -> Unit) -> Unit,
    onVolver: () -> Unit
) {
    var identificador by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var procesando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar sesión",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = identificador,
            onValueChange = {
                identificador = it
                error = ""
            },
            label = { Text("Nombre o correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error.isNotBlank()
        )

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = !procesando,
            onClick = {
                if (identificador.isBlank()) {
                    error = "Ingresa tu nombre o correo."
                } else {
                    error = ""
                    procesando = true

                    onIniciarSesion(identificador.trim()) { valido, mensaje ->
                        procesando = false
                        error = if (valido) "" else mensaje
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (procesando) "Verificando..." else "Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
