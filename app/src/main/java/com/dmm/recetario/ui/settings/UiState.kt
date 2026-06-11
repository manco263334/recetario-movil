package com.dmm.recetario.ui.settings

sealed interface SettingsUiState {
    data class Loading(val message: String) : SettingsUiState
    object Success : SettingsUiState
    data class Error(val message: String) : SettingsUiState
}