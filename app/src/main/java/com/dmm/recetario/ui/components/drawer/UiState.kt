package com.dmm.recetario.ui.components.drawer

sealed interface LogOutUiState {
    object Idle : LogOutUiState
    object Loading : LogOutUiState
    data class Success(val message: String) : LogOutUiState
    data class Error(val message: String) : LogOutUiState
}