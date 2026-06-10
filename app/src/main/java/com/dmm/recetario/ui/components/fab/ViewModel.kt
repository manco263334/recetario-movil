package com.dmm.recetario.ui.components.fab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.service.CategoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FABViewModel @Inject constructor (
    private val categoryService: CategoryService
) : ViewModel() {
    val categories = categoryService
        .getAllCategories(1, 10, false)
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
}