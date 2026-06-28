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
    data class RecipeFormViewModel (
        val name: String,
        val persons: Int,
        val icon: String?,
        val steps: List<String>,
        val totalTimeInMinutes: Int,
        val cookingTimeInMinutes: Int,
        val preparationTimeInMinutes: Int,
        val ingredients: List<Map<String, String>>
    )

    var data by mutableStateOf (
        RecipeFormViewModel (
            "", 0, null, emptyList(),
            0, 0, 0,
            emptyList()
        )
    )

    val categories = categoryService
        .getAllCategories(0, 10 ,false)
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var recipe: Recipe? by mutableStateOf(null)

    fun addRecipeData() {
        recipe = Recipe (
            id = "",
            stars = 0,
            icon = null,
            name = data.name,
            steps = data.steps,
            persons = data.persons,
            ingredients = data.ingredients,
            totalTimeInMinutes = data.totalTimeInMinutes,
            cookingTimeInMinutes = data.cookingTimeInMinutes,
            preparationTimeInMinutes = data.preparationTimeInMinutes,

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

    fun updateName(name: String) {
        data = data.copy(name = name)
    }

    fun updatePersons(persons: String) {
        data = data.copy(persons = persons.toIntOrNull() ?: 0)
    }

    fun updateIcon(icon: String) {
        data = data.copy(icon = icon.ifBlank { null })
    }

    fun addStep(step: String) {
        data = data.copy(steps = data.steps.plus(step))
    }

    fun clearSteps() {
        data = data.copy(steps = emptyList())
    }

    fun removeLastStep() {
        data = data.copy(steps = data.steps.dropLast(1))
    }

    fun updateTotalTimeInMinutes(totalTimeInMinutes: String) {
        data = data.copy(totalTimeInMinutes = totalTimeInMinutes.toIntOrNull() ?: 0)
    }

    fun updateCookingTimeInMinutes(cookingTimeInMinutes: String) {
        data = data.copy(cookingTimeInMinutes = cookingTimeInMinutes.toIntOrNull() ?: 0)
    }

    fun updatePreparationTimeInMinutes(preparationTimeInMinutes: String) {
        data = data.copy(preparationTimeInMinutes = preparationTimeInMinutes.toIntOrNull() ?: 0)
    }

    fun addIngredient(ingredient: Map<String, String>) {
        data = data.copy(ingredients = data.ingredients.plus(ingredient))
    }

    fun clearIngredients() {
        data = data.copy(ingredients = emptyList())
    }

    fun removeLastIngredient() {
        data = data.copy(ingredients = data.ingredients.dropLast(1))
    }
}