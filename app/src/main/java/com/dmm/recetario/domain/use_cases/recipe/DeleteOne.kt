package com.dmm.recetario.domain.use_cases.recipe

import android.util.Log
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.domain.repository.RecipeRepository
import jakarta.inject.Inject

class DeleteRecipeUseCase @Inject constructor (
    private val repository: RecipeRepository,
    private val dao: RecipeDao<RecipeEntity, RecipeCategoryCrossRef, UserEntity, CategoryEntity>
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