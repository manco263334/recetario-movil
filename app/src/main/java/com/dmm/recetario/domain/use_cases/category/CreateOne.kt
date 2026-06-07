package com.dmm.recetario.domain.use_cases.category

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.repository.CategoryRepository
import jakarta.inject.Inject

open class CreateCategoryUseCase @Inject constructor (
    private val repository: CategoryRepository,
    private val dao: CategoryDao<CategoryEntity, RecipeCategoryCrossRef, RecipeEntity>
) {
    suspend operator fun invoke(data: Category): Category? {
        var category: Category?

        try {
            category = repository.createCategory(data)
        } catch (e: APIException) {
            Log.w("CreateCategoryUseCase", "${e.message}")
            return null
        }

        if (category.isNotNull()) {
            dao.saveCategories(listOf(category.toEntity()))
        }

        return category
    }
}