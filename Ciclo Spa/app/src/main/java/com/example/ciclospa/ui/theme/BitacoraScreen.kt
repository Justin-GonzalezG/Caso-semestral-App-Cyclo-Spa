package com.example.ciclospa.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciclospa.viewmodels.BitacoraViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraScreen(
    bitacoraViewModel: BitacoraViewModel = viewModel(),
    onVolver: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bitácora Diaria - Ciclo Spa") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registra tu día",
                style = MaterialTheme.typography.headlineMedium
            )

            // Día del ciclo
            OutlinedTextField(
                value = bitacoraViewModel.diaCiclo,
                onValueChange = { bitacoraViewModel.diaCiclo = it },
                label = { Text("Día actual del ciclo (ej. 14)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Selección de estado de cristalización
            Text(
                text = "Estado de cristalización (Saliva):",
                style = MaterialTheme.typography.titleMedium
            )

            bitacoraViewModel.opcionesCristalizacion.forEach { opcion ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = bitacoraViewModel.cristalizacionSeleccionada == opcion,
                        onClick = { bitacoraViewModel.cristalizacionSeleccionada = opcion }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Notas o síntomas opcionales
            OutlinedTextField(
                value = bitacoraViewModel.notas,
                onValueChange = { bitacoraViewModel.notas = it },
                label = { Text("Notas o síntomas personales (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Guardar
            Button(
                onClick = {
                    if (bitacoraViewModel.diaCiclo.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor ingresa el día del ciclo")
                        }
                    } else {
                        bitacoraViewModel.guardarRegistro {
                            scope.launch {
                                snackbarHostState.showSnackbar("¡Registro guardado con éxito!")
                            }
                            onVolver()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Registro Diario")
            }
        }
    }
}