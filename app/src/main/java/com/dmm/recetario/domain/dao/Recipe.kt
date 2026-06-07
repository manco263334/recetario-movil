package com.dmm.recetario.domain.dao

import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import com.dmm.recetario.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface RecipeDao <
    IRecipeEntity : RecipeEntity,
    IRecipeCategoryCrossRef : RecipeCategoryCrossRef,
    IUserEntity : UserEntity,
    ICategoryEntity : CategoryEntity
> {
    fun getRecipes(): Flow<List<IRecipeEntity>>

    fun getRecipe(id: String): Flow<IRecipeEntity?>

    suspend fun saveRecipes(recipes: List<IRecipeEntity>)

    suspend fun saveRecipe(recipe: IRecipeEntity)

    suspend fun insertReferences(refs: List<IRecipeCategoryCrossRef>)

    suspend fun insertReference(ref: IRecipeCategoryCrossRef)

    suspend fun clear()

    suspend fun deleteRecipe(id: String)

    fun getUser(recipeId: String): Flow<IUserEntity?>

    fun getCategories(recipeId: String): Flow<List<ICategoryEntity>>
}