package com.example.fitnesstracker.ui.menu

import androidx.lifecycle.ViewModel
import com.example.fitnesstracker.data.MainMenuUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainMenuViewModel : ViewModel() {
    private val _menuState = MutableStateFlow(MainMenuUIState())
    val menuState: StateFlow<MainMenuUIState> = _menuState
}