package com.dmm.recetario.ui.recipe

sealed interface RecipeUiState {
    object Loading : RecipeUiState
    object Success : RecipeUiState
    data class Error(val message: String) : RecipeUiState
}