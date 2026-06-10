package com.dmm.recetario.ui.components.refresher

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RefresherViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf<RefresherUiState>(RefresherUiState.Idle)
        private set

    fun refresh(call: suspend () -> Unit) {
        if (uiState is RefresherUiState.Loading) return

        viewModelScope.launch {
            uiState = RefresherUiState.Loading

            try {
                call()
                uiState = RefresherUiState.Success("Elements refreshed successfully")
            } catch (e: Exception) {
                Log.d("RefresherViewModel", "Error refrescando elementos: ${e.message}", e)
                uiState = RefresherUiState.Error("Error while refreshing elements")
            } finally {
                delay(3.seconds)
                uiState = RefresherUiState.Idle
            }
        }
    }
}