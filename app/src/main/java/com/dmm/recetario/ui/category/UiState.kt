package com.dmm.recetario.ui.category

sealed interface CategoryUiState {
    data class Loading(val message: String) : CategoryUiState
    object Success : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}