package com.dmm.recetario.domain.use_cases.recipe

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.repository.RecipeRepository
import jakarta.inject.Inject

class CreateRecipeUseCase @Inject constructor (
    private val repository: RecipeRepository,
    private val dao: RecipeDao
) {
    suspend operator fun invoke(data: Recipe): Recipe? {
        var recipe: Recipe?

        try {
            recipe = repository.createRecipe(data)
        } catch (e: APIException) {
            Log.w("CreateRecipeUseCase", "${e.message}")
            return null
        }

        if (recipe.isNotNull()) {
            dao.saveRecipe(recipe.toEntity())
        }

        return recipe
    }
}