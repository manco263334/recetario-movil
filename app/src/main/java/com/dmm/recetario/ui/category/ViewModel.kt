package com.dmm.recetario.ui.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.service.CategoryService
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
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@HiltViewModel
class CategoryViewModel @Inject constructor (
    private val categoryService: CategoryService,
    private val recipeService: RecipeService
) : ViewModel() {
    var uiState: CategoryUiState by mutableStateOf (
        CategoryUiState.Loading("Cargando recetas...")
    )
        private set

    private val _selectedCategoryId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipes: StateFlow<List<Recipe>> = _selectedCategoryId
        .flatMapLatest { categoryId ->
            if (categoryId == null) {
                flowOf(emptyList())
            } else {
                categoryService.getRecipes(categoryId)
            }
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
        .also { uiState = CategoryUiState.Success }

    init {
        sync()
    }

    fun loadRecipes(categoryId: String) {
        _selectedCategoryId.value = categoryId
    }

    fun sync (
        page: Int = 0,
        size: Int = 10
    ) {
        viewModelScope.launch {
            recipeService.syncRecipes (
                page = page,
                size = size,
                withCategories = true,
                withCreator = false
            )
            categoryService.syncCategories(page, size, true)
        }
    }

    suspend fun refresh() {
        if (uiState is CategoryUiState.Loading) return

        uiState = CategoryUiState.Loading("Actualizando y cargando nuevas recetas...")
        val result = _selectedCategoryId.value?.let {
            categoryService.syncCategory(it, true)
        }

        when (result) {
            false -> {
                val message = "Error sincronizando la categoría"
                uiState = CategoryUiState.Error(message)
                throw Exception(message)
            }
            null -> {
                val message = "Categoría no encontrada"
                uiState = CategoryUiState.Error(message)
                throw Exception(message)
            }
            else -> {
                uiState = CategoryUiState.Success
            }
        }
    }
}