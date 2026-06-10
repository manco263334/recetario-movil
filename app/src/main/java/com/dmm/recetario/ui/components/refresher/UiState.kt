package com.dmm.recetario.ui.components.refresher

sealed interface RefresherUiState {
    object Idle : RefresherUiState
    object Loading : RefresherUiState
    data class Success(val message: String) : RefresherUiState
    data class Error(val message: String) : RefresherUiState
}