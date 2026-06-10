package com.dmm.recetario.ui.home

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Success : HomeUiState
    data class Error(val message: String) : HomeUiState
}