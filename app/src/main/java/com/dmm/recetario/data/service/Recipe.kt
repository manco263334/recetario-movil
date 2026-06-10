package com.dmm.recetario.data.service

import android.util.Log
import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.domain.exception.APIException
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.core.utils.mapper.toEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRefImpl
import com.dmm.recetario.domain.dao.RecipeDao
import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.UserEntity
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.repository.RecipeRepository
import com.dmm.recetario.domain.service.RecipeService
import com.dmm.recetario.domain.use_cases.recipe.CreateRecipeUseCase
import com.dmm.recetario.domain.use_cases.recipe.DeleteRecipeUseCase
import com.dmm.recetario.domain.use_cases.recipe.UpdateRecipeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeServiceImpl (
    private val createRecipeUseCase: CreateRecipeUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val repository: RecipeRepository,
    private val dao: RecipeDao<RecipeEntity, RecipeCategoryCrossRef, UserEntity, CategoryEntity>
) : RecipeService {
    override suspend fun createRecipe(data: Recipe): Recipe {
        val recipe = createRecipeUseCase(data)

        assert(recipe.isNotNull())

        return recipe!!
    }

    override fun getAllRecipes (
        page: Int,
        size: Int,
        withCategories: Boolean?,
        withCreator: Boolean?
    ): Flow<List<Recipe>> {
        return dao.getRecipes().map { recipes ->
            recipes.map { recipe ->
                recipe.toDomain()
            }
        }
    }

    override suspend fun syncRecipes (
        page: Int,
        size: Int,
        withCategories: Boolean?,
        withCreator: Boolean?
    ): Boolean {
        return try {
            val recipes = repository.getAllRecipes (
                page = page,
                size = size,
                withCategories = withCategories,
                withCreator = withCreator
            )

            dao.saveRecipes(recipes.map { it.toEntity() })

            if (withCategories == true) {
                recipes.forEach { recipe ->
                    dao.insertReferences(recipe.categories?.map { categoryId ->
                        RecipeCategoryCrossRefImpl(recipe.id, categoryId)
                    } ?: emptyList())
                }
            }

            true
        } catch (e: APIException) {
            Log.e("RecipeService", "Error syncing recipes: ${e.message}", e)
            false
        }
    }

    override suspend fun syncRecipe (
        id: String,
        withCategories: Boolean?,
        withCreator: Boolean?
    ): Boolean {
        return try {
            val recipe = repository.getRecipe(id, withCategories, withCreator)

            dao.saveRecipe(recipe.toEntity())

            if (withCategories == true) {
                dao.insertReferences(recipe.categories?.map {
                    RecipeCategoryCrossRefImpl(recipe.id, it)
                } ?: emptyList())
            }

            true
        } catch (e: APIException) {
            Log.e("RecipeService", "Error syncing recipe: ${e.message}", e)
            false
        }
    }

    override fun getRecipe(id: String): Flow<Recipe?> {
        return dao.getRecipe(id).map { recipe ->
            recipe?.toDomain()
        }
    }

    override suspend fun updateRecipe (
        id: String,
        data: Recipe
    ): Recipe {
        val recipe = updateRecipeUseCase(id, data)

        assert(recipe.isNotNull())

        return recipe!!
    }

    override suspend fun deleteRecipe(id: String) {
        assert(deleteRecipeUseCase(id))
    }
}