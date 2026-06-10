package com.dmm.recetario.ui.auth.login

sealed interface LoginUiState {
    object Idle : LoginUiState

    object Loading : LoginUiState

    data class Success(val token: String) : LoginUiState

    data class Error(val message: String) : LoginUiState
}