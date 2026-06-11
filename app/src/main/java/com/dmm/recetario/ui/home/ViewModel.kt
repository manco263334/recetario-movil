package com.dmm.recetario.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.service.CategoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val categoryService: CategoryService
) : ViewModel() {
    var uiState: HomeUiState by mutableStateOf (
        HomeUiState.Loading("Cargando categorías...")
    )
        private set

    val categories = categoryService
        .getAllCategories(1, 10, false)
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        .also { uiState = HomeUiState.Success }

    init {
        sync()
    }

    fun sync (
        page: Int = 0,
        size: Int = 10
    ) {
        viewModelScope.launch {
            categoryService.syncCategories (
                page,
                size,
                withRecipes = true
            )
        }
    }

    suspend fun refresh () {
        if (uiState is HomeUiState.Loading) return

        uiState = HomeUiState.Loading("Actualizando y cargando nuevas categorías...")
        val result = categoryService.syncCategories(1, 10, withRecipes = true)

        if (!result) {
            val message = "Error sincronizando las categorías"
            uiState = HomeUiState.Error(message)
            throw Exception(message)
        } else {
            uiState = HomeUiState.Success
        }
    }
}