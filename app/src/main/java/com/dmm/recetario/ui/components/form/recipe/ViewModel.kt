package com.dmm.recetario.ui.components.form.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.service.CategoryService
import com.dmm.recetario.domain.service.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeFormViewModel @Inject constructor (
    private val categoryService: CategoryService,
    private val recipeService: RecipeService
) : ViewModel() {
    val categories = categoryService
        .getAllCategories(1, 10 ,false)
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var recipe by mutableStateOf<Recipe?>(null)

    fun addRecipeData (
        name: String,
        persons: Int,
        ingredients: List<Map<String, String>>,
        steps: List<String>,
        totalTimeInMinutes: Int,
        cookingTimeInMinutes: Int,
        preparationTimeInMinutes: Int,
    ) {
        recipe = Recipe (
            id = "",
            name = name,
            persons = persons,
            ingredients = ingredients,
            steps = steps,
            totalTimeInMinutes = totalTimeInMinutes,
            cookingTimeInMinutes = cookingTimeInMinutes,
            preparationTimeInMinutes = preparationTimeInMinutes,
            stars = 0,
            icon = null,

            creator = null,
            categories = null
        )
    }

    fun createRecipe(categories: List<String>) {
        viewModelScope.launch {
            assert(recipe.isNotNull())

            recipe!!.categories = categories

            recipeService.createRecipe(recipe!!)
        }
    }
}