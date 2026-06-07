package com.dmm.recetario.domain.dao

import com.dmm.recetario.data.local.database.entity.CategoryEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.data.local.database.entity.RecipeEntity
import com.dmm.recetario.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface RecipeDao {
    fun getRecipes(): Flow<List<RecipeEntity>>

    fun getRecipe(id: String): Flow<RecipeEntity?>

    suspend fun saveRecipes(recipes: List<RecipeEntity>)

    suspend fun saveRecipe(recipe: RecipeEntity)

    suspend fun insertReferences(refs: List<RecipeCategoryCrossRef>)

    suspend fun insertReference(ref: RecipeCategoryCrossRef)

    suspend fun clear()

    suspend fun deleteRecipe(id: String)

    fun getUser(recipeId: String): Flow<UserEntity?>

    fun getCategories(recipeId: String): Flow<List<CategoryEntity>>
}