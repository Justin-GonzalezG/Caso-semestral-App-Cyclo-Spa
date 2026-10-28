package com.example.ciclospa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ciclospa.navigation.Screen
import com.example.ciclospa.ui.theme.BitacoraScreen
import com.example.ciclospa.ui.theme.CicloSpaTheme
import com.example.ciclospa.ui.theme.DashboardScreen
import com.example.ciclospa.ui.theme.RegisterScreen
import com.example.ciclospa.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CicloSpaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val mainViewModel: MainViewModel = viewModel()
                    var nombreRegistrado by remember { mutableStateOf("") }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = mainViewModel,
                                onNavigateToRegister = {
                                    navController.navigate(Screen.Register.route)
                                },
                                onNavigateToBitacora = {
                                    navController.navigate("bitacora")
                                }
                            )
                        }

                        composable(Screen.Register.route) {
                            RegisterScreen(
                                onRegistroExitoso = { nombre ->
                                    nombreRegistrado = nombre
                                    navController.navigate("dashboard") {
                                        popUpTo(Screen.Register.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("bitacora") {
                            BitacoraScreen(
                                onVolver = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("dashboard") {
                            DashboardScreen(
                                nombre = nombreRegistrado,
                                onCerrarSesion = {
                                    nombreRegistrado = ""
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}