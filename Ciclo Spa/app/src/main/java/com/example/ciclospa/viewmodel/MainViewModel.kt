
package com.example.ciclospa.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciclospa.navigation.NavigationEvent
import com.example.ciclospa.navigation.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _navigationEvents =
        Channel<NavigationEvent>(Channel.BUFFERED)

    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateTo(screen)
            )
        }
    }

    fun popBackStack() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.PopBackStack
            )
        }
    }
}