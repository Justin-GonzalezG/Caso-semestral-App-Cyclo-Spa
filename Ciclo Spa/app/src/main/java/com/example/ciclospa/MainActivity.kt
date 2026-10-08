
package com.example.ciclospa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ciclospa.navigation.NavigationEvent
import com.example.ciclospa.navigation.Screen
import com.example.ciclospa.ui.theme.RegisterScreen

import com.example.ciclospa.ui.theme.CicloSpaTheme
import com.example.ciclospa.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CicloSpaTheme {
                val mainViewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route)
                            }

                            NavigationEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(viewModel = mainViewModel)
                        }

                        composable(Screen.Register.route) {
                            RegisterScreen()
                        }
                    }
                }
            }
        }
    }
}
