package com.dmm.recetario.domain.use_cases.category

import android.util.Log
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.domain.repository.CategoryRepository
import jakarta.inject.Inject

class DeleteCategoryUseCase @Inject constructor (
    private val repository: CategoryRepository,
    private val dao: CategoryDao<CategoryEntity, RecipeCategoryCrossRef, RecipeEntity>
) {
    suspend operator fun invoke(id: String): Boolean {
        try {
            repository.deleteCategory(id)
        } catch (e: APIException) {
            Log.w("DeleteCategoryUseCase", "${e.message}")
            return false
        }

        dao.deleteCategory(id)

        return true
    }
}