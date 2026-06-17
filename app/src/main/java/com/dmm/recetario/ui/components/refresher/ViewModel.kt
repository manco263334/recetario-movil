package com.dmm.recetario.ui.components.refresher

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RefresherViewModel @Inject constructor (
    private val resourceHelper: ResourceHelper
) : ViewModel() {
    private val getString: (Int) -> String = resourceHelper::getString

    var uiState: RefresherUiState by mutableStateOf(RefresherUiState.Idle)
        private set

    fun refresh(call: suspend () -> Unit) {
        if (uiState is RefresherUiState.Loading) return

        viewModelScope.launch {
            uiState = RefresherUiState.Loading

            uiState = try {
                call()
                 RefresherUiState.Success (
                    getString(R.string.elements_refreshed_succeed)
                )
            } catch (e: Exception) {
                val message = getString(R.string.elements_refreshed_failed)
                Log.d("RefresherViewModel", "$message: ${e.message}", e)
                RefresherUiState.Error(message)
            }
        }
    }
}