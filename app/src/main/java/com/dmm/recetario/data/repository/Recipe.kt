package com.dmm.recetario.data.repository

import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.handler.handleApiCall
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.data.remote.retrofit.RecipeRemote
import com.dmm.recetario.domain.model.Recipe
import com.dmm.recetario.domain.repository.RecipeRepository

class RecipeRepositoryImpl (
    private val remote: RecipeRemote
) : RecipeRepository {
    override suspend fun createRecipe (data: Recipe): Recipe {
        val recipe = handleApiCall { remote.createRecipe(data) }

        assert(recipe.isNotNull())

        return recipe!!.toDomain()
    }

    override suspend fun getAllRecipes (
        page: Int,
        size: Int,
        withCategories: Boolean?,
        withCreator: Boolean?
    ): List<Recipe> {
        val recipes = handleApiCall {
            remote.getAllRecipes (
                page = page,
                size = size,
                withCategories = withCategories,
                withCreator = withCreator
            )
        }

        return recipes?.content?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getRecipe (
        id: String,
        withCategories: Boolean?,
        withCreator: Boolean?
    ): Recipe {
        val recipe = handleApiCall {
            remote.getRecipe (
                id = id,
                withCategories = withCategories,
                withCreator = withCreator
            )
        }

        assert(recipe.isNotNull())

        return recipe!!.toDomain()
    }

    override suspend fun updateRecipe (
        id: String,
        data: Recipe
    ): Recipe {
        val recipe = handleApiCall { remote.updateRecipe(id, data) }

        assert(recipe.isNotNull())

        return recipe!!.toDomain()
    }

    override suspend fun deleteRecipe(id: String) {
        handleApiCall { remote.deleteRecipe(id) }
    }
}