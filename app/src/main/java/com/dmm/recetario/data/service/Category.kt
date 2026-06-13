package com.dmm.recetario.data.service

import android.util.Log
import com.dmm.recetario.R
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRefImpl
import com.dmm.recetario.domain.dao.CategoryDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
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
    private val repository: CategoryRepository,
    private val resourceHelper: ResourceHelper,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val dao: CategoryDao<CategoryEntity, RecipeCategoryCrossRef, RecipeEntity>
) : CategoryService {
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
        return dao.getRecipes(categoryId = categoryId).map { entities ->
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

            dao.saveCategories (
                categories = categories.map {
                    it.toEntity()
                }
            )

            if (withRecipes == true) {
                categories.forEach { category ->
                    dao.insertReferences (
                        refs = category.recipes?.map { recipeId ->
                            RecipeCategoryCrossRefImpl (
                                recipeId = recipeId,
                                categoryId = category.id
                            )
                        } ?: emptyList()
                    )
                }
            }

            true
        } catch (e: APIException) {
            val message = resourceHelper.getString(R.string.refreshing_categories_failed)
            Log.e("CategoryService", "$message: ${e.message}", e)
            false
        }
    }

    override suspend fun syncCategory(id: String, withRecipes: Boolean?): Boolean {
        return try {
            val category = repository.getCategory (
                id = id,
                withRecipes = withRecipes
            )

            dao.saveCategory(category = category.toEntity())

            if (withRecipes == true) {
                dao.insertReferences (
                    refs = category.recipes?.map { recipeId ->
                        RecipeCategoryCrossRefImpl (
                            recipeId = recipeId,
                            categoryId = category.id
                        )
                    } ?: emptyList()
                )
            }

            true
        } catch (e: APIException) {
            val message = resourceHelper.getString(R.string.refreshing_category_failed)
            Log.e("CategoryService", "$message: ${e.message}", e)
            false
        }
    }

    override fun getCategory(id: String, withRecipes: Boolean?): Flow<Category?> {
        return dao.getCategory(id = id).map { category ->
            category?.toDomain()
        }
    }

    override suspend fun updateCategory(id: String, data: Category): Category {
        val category = updateCategoryUseCase(id, data)

        assert(category.isNotNull())

        return category!!
    }

    override suspend fun deleteCategory(id: String) {
        assert(deleteCategoryUseCase(id))
    }
}