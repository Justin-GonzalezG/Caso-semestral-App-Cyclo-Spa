
package com.example.ciclospa.navigation

sealed class NavigationEvent {

    data class NavigateTo(
        val route: Screen
    ) : NavigationEvent()

    data object PopBackStack : NavigationEvent()
}
