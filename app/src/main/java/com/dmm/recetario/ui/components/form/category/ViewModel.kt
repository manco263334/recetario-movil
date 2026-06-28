package com.dmm.recetario.ui.components.form.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.service.CategoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryFormViewModel @Inject constructor (
    private val categoryService: CategoryService
) : ViewModel() {
    data class CategoryFormViewModel(val name: String, val icon: String?)

    var data by mutableStateOf(CategoryFormViewModel("", null))
        private set

    fun createCategory() {
        viewModelScope.launch {
            val data = Category (
                id = "",
                name = data.name,
                icon = data.icon,

                recipes = null
            )
            categoryService.createCategory(data)
        }
    }

    fun updateName(name: String) {
        data = data.copy(name = name)
    }

    fun updateIcon(icon: String) {
        data = data.copy(icon = icon.ifBlank { null })
    }
}