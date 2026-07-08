package com.dmm.recetario.ui.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.service.CategoryService
import com.dmm.recetario.domain.service.RecipeService
import com.dmm.recetario.ui.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@HiltViewModel
class CategoryViewModel @Inject constructor (
    private val recipeService: RecipeService,
    private val resourceHelper: ResourceHelper,
    private val categoryService: CategoryService
) : BaseViewModel() {
    private val getString: (Int) -> String = resourceHelper::getString

    var uiState: CategoryUiState by mutableStateOf (
        CategoryUiState.Loading (
            getString(R.string.loading_recipes)
        )
    )
        private set

    var page by mutableIntStateOf(0)
        private set

    private val _selectedCategoryId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipes: StateFlow<List<Recipe>> = _selectedCategoryId
        .flatMapLatest { categoryId ->
            if (categoryId == null) {
                recipeService.getAllRecipes(page, 10, false, false)
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

    fun sync(size: Int = 10) {
        viewModelScope.launch {
            recipeService.syncRecipes (
                page = page,
                size = size,
                withCreator = false,
                withCategories = true
            )
            categoryService.syncCategories(page, size, true)
        }
    }

    suspend fun refresh() {
        if (uiState is CategoryUiState.Loading) return

        uiState = CategoryUiState.Loading (
            getString(R.string.refreshing_recipes)
        )
        val result = _selectedCategoryId.value?.let {
            categoryService.syncCategory(it, true)
        }

        when (result) {
            false -> {
                val message = getString(R.string.refreshing_category_failed)
                uiState = CategoryUiState.Error(message)
                throw Exception(message)
            }
            null -> {
                val message = getString(R.string.category_not_found)
                uiState = CategoryUiState.Error(message)
                throw Exception(message)
            }
            else -> {
                uiState = CategoryUiState.Success
            }
        }
    }

    fun incrementePage() {
        page++
    }

    fun decrementPage() {
        page--
    }
}