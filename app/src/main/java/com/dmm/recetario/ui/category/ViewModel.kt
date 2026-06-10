package com.dmm.recetario.ui.category

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
        val result = _selectedCategoryId.value?.let {
            categoryService.syncCategory(it, true)
        }

        if (result == false) {
            throw Exception("Error sincronizando las recetas")
        } else if (result == null) {
            throw Exception("Categoría no seleccionada")
        }
    }
}