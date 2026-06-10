package com.dmm.recetario.ui.components.form.category

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
    fun createCategory(name: String, icon: String?) {
        viewModelScope.launch {
            val data = Category (
                id = "",
                name = name,
                icon = icon,

                recipes = null
            )
            categoryService.createCategory(data)
        }
    }
}