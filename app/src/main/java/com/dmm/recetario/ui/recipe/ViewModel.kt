package com.dmm.recetario.ui.recipe

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

    fun loadRecipe(recipeId: String) {
        _selectedRecipeId.value = recipeId
    }

    suspend fun refresh() {
        val result = _selectedRecipeId.value?.let {
            recipeService.syncRecipe(it, false, false)
        }

        if (result == false) {
            throw Exception("Error sincronizando la receta")
        } else if (result == null) {
            throw Exception("Receta no seleccionada")
        }
    }
}