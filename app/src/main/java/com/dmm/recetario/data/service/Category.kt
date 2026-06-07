package com.dmm.recetario.data.service

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exceptions.APIException
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.repository.CategoryRepository
import com.dmm.recetario.domain.service.CategoryService
import com.dmm.recetario.domain.use_cases.category.CreateCategoryUseCase
import com.dmm.recetario.domain.use_cases.category.DeleteCategoryUseCase
import com.dmm.recetario.domain.use_cases.category.UpdateCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryServiceImpl (
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val repository: CategoryRepository,
    private val dao: CategoryDao
): CategoryService {
    override suspend fun createCategory(data: Category): Category {
        val category = createCategoryUseCase(data)

        assert(category.isNotNull())

        return category!!
    }

    override fun getAllCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Flow<List<Category>> {
        return dao.getCategories().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getRecipes(categoryId: String): Flow<List<Recipe>> {
        return dao.getRecipes(categoryId).map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun syncCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Boolean {
        return try {
            val categories = repository.getAllCategories (
                page = page,
                size = size,
                withRecipes = withRecipes
            )

            dao.saveCategories(categories.map { it.toEntity() })

            if (withRecipes == true) {
                categories.forEach { category ->
                    dao.insertReferences(category.recipes?.map { recipeId ->
                        RecipeCategoryCrossRef(recipeId, category.id)
                    } ?: emptyList())
                }
            }

            true
        } catch (e: APIException) {
            Log.e("CategoryService", "Error syncing categories: ${e.message}", e)
            false
        }
    }

    override suspend fun syncCategory(id: String, withRecipes: Boolean?): Boolean {
        return try {
            val category = repository.getCategory(id, withRecipes)

            dao.saveCategory(category.toEntity())

            if (withRecipes == true) {
                dao.insertReferences(category.recipes?.map {
                    RecipeCategoryCrossRef(it, category.id)
                } ?: emptyList())
            }

            true
        } catch (e: APIException) {
            Log.e("CategoryService", "Error syncing category: ${e.message}", e)
            false
        }
    }

    override fun getCategory (
        id: String,
        withRecipes: Boolean?)
    : Flow<Category?> {
        return dao.getCategory(id).map { category ->
            category?.toDomain()
        }
    }

    override suspend fun updateCategory (
        id: String,
        data: Category
    ): Category {
        val category = updateCategoryUseCase(id, data)

        assert(category.isNotNull())

        return category!!
    }

    override suspend fun deleteCategory(id: String) {
        assert(deleteCategoryUseCase(id))
    }
}