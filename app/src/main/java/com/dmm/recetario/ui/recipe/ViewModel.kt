package com.dmm.recetario.ui.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.service.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RecipeViewModel @Inject constructor (
    private val recipeService: RecipeService
) : ViewModel() {
    var uiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading("Cargando receta..."))
        private set

    private val _selectedRecipeId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipe: StateFlow<Recipe?> = _selectedRecipeId
        .flatMapLatest { recipeId ->
            if (recipeId == null) {
                flowOf(null)
            } else {
                recipeService.getRecipe(recipeId)
            }
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
        .also { uiState = RecipeUiState.Success }

    fun loadRecipe(recipeId: String) {
        _selectedRecipeId.value = recipeId
    }

    suspend fun refresh() {
        if (uiState is RecipeUiState.Loading) return

        uiState = RecipeUiState.Loading("Actualizando y cargando la receta...")
        val result = _selectedRecipeId.value?.let {
            recipeService.syncRecipe(it, false, false)
        }

        when (result) {
            false -> {
                val message = "Error sincronizando la receta"
                uiState = RecipeUiState.Error(message)
                throw Exception(message)
            }
            null -> {
                val message = "Receta no encontrada"
                uiState = RecipeUiState.Error(message)
                throw Exception(message)
            }
            else -> {
                uiState = RecipeUiState.Success
            }
        }
    }
}