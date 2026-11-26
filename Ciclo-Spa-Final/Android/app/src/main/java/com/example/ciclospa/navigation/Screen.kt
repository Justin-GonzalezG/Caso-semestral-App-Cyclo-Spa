
package com.example.ciclospa.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_page")

    data object Register : Screen("register_page")

    data object Login : Screen("login_page")

    data object Bitacora : Screen("bitacora_page")

    // Nueva pantalla para consumir API REST
    data object Posts : Screen("posts_page")

    data object BackendContenidos :
        Screen("backend_contenidos_page")

}
