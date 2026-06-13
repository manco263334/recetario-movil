package com.dmm.recetario.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.service.CategoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val resourceHelper: ResourceHelper,
    private val categoryService: CategoryService
) : ViewModel() {
    private val getString: (Int) -> String = resourceHelper::getString
    
    var uiState: HomeUiState by mutableStateOf (
        HomeUiState.Loading (
            getString(R.string.loading_categories)
        )
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

        uiState = HomeUiState.Loading (
            getString(R.string.refreshing_categories)
        )
        val result = categoryService.syncCategories(1, 10, withRecipes = true)

        if (!result) {
            val message = getString(R.string.refreshing_categories_failed)
            uiState = HomeUiState.Error(message)
            throw Exception(message)
        } else {
            uiState = HomeUiState.Success
        }
    }
}