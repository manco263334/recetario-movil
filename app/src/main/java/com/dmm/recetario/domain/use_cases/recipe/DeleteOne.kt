package com.dmm.recetario.domain.use_cases.recipe

import android.util.Log
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.domain.repository.RecipeRepository
import jakarta.inject.Inject

class DeleteRecipeUseCase @Inject constructor (
    private val repository: RecipeRepository,
    private val dao: RecipeDao
) {
    suspend operator fun invoke(id: String): Boolean {
        try {
            repository.deleteRecipe(id)
        } catch (e: APIException) {
            Log.w("DeleteRecipeUseCase", "${e.message}")
            return false
        }

        dao.deleteRecipe(id)

        return true
    }
}