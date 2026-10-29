
package com.example.ciclospa

import androidx.compose.foundation.Image

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciclospa.navigation.Screen
import com.example.ciclospa.viewmodels.MainViewModel

private val Crema = Color(0xFFFFF9F5)
private val RosaEmpolvado = Color(0xFFB86F85)
private val Malva = Color(0xFF754657)
private val VerdeSalvia = Color(0xFF7E907A)
private val TextoOscuro = Color(0xFF392B32)

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
    ) {
        Image(
            painter = painterResource(R.drawable.images),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Crema.copy(alpha = 0.82f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Surface(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(10.dp, CircleShape),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.96f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Emblema de Ciclo Spa",
                            tint = RosaEmpolvado,
                            modifier = Modifier.size(45.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Ciclo Spa",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Malva
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Ciclo Spa",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Malva,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "BIENESTAR EN TU RITMO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeSalvia,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(RosaEmpolvado, VerdeSalvia)
                        ),
                        shape = RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tu bienestar,\ntu espacio, tu ritmo.",
                fontSize = 26.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))



            Text(
                text = "Tu espacio para registrar tu ciclo, guardar tus recuerdos y cuidar de tu bienestar cada día.",
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFB85D82),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )


            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    viewModel.navigateTo(Screen.Register)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(5.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeSalvia,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.size(9.dp))

                Text(
                    text = "Crear mi cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            OutlinedButton(
                onClick = {
                    viewModel.navigateTo(Screen.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Malva
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.size(9.dp))

                Text(
                    text = "Ya tengo una cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RosaEmpolvado,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.size(7.dp))

                Text(
                    text = "Un momento para ti, cada día",
                    fontSize = 12.sp,
                    color = Malva,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
