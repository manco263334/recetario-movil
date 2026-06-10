package com.dmm.recetario.ui.category

sealed interface CategoryUiState {
    object Loading : CategoryUiState
    object Success : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}