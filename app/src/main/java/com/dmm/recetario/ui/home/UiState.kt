package com.dmm.recetario.ui.home

sealed interface HomeUiState {
    data class Loading(val message: String) : HomeUiState
    object Success : HomeUiState
    data class Error(val message: String) : HomeUiState
}