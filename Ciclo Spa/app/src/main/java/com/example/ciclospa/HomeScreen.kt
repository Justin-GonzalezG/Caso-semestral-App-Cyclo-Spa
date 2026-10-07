package com.example.ciclospa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclospa.navigation.Screen
import com.example.ciclospa.viewmodels.MainViewModel

@Composable

fun HomeScreen(viewModel: MainViewModel) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.images),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // La Column principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 1) Logo de la app EVA
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo EVA myCiclo",
                modifier = Modifier
                    .size(130.dp) // Tamaño del logo
                    .padding(bottom = 24.dp), // Espacio entre el logo y el título
                contentScale = ContentScale.Fit
            )

            // 2) Título
            Text(
                text = "¡Bienvenido a Ciclo Spa!",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 3) Descripción
            Text(
                text = "Tu bitácora digital privada y segura para el seguimiento de tu ciclo y bienestar diario mediante el dispositivo EVA.",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // 4) Botón Registrar
            Button(
                onClick = {
                    viewModel.navigateTo(Screen.Register)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Registrar", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5) Botón Ya tengo Cuenta
            OutlinedButton(
                onClick = { /* Accion Ya tengo Cuenta */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Ya tengo Cuenta", fontSize = 18.sp)
            }
        }
    }
}
