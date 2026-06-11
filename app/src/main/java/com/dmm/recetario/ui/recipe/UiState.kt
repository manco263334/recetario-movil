package com.dmm.recetario.ui.recipe

sealed interface RecipeUiState {
    data class Loading(val message: String) : RecipeUiState
    object Success : RecipeUiState
    data class Error(val message: String) : RecipeUiState
}