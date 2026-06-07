package com.dmm.recetario.domain.dao

import com.dmm.recetario.domain.entity.CategoryEntity
import com.dmm.recetario.domain.entity.RecipeCategoryCrossRef
import com.dmm.recetario.domain.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface CategoryDao <
    ICategoryEntity : CategoryEntity,
    IRecipeCategoryCrossRef : RecipeCategoryCrossRef,
    IRecipeEntity : RecipeEntity
> {
    fun getCategories(): Flow<List<ICategoryEntity>>

    fun getCategory(id: String): Flow<ICategoryEntity?>

    suspend fun saveCategories(categories: List<ICategoryEntity>)

    suspend fun saveCategory(category: ICategoryEntity)

    suspend fun insertReferences(refs: List<IRecipeCategoryCrossRef>)

    suspend fun insertReference(ref: IRecipeCategoryCrossRef)

    suspend fun clear()

    suspend fun deleteCategory(id: String)

    fun getRecipes(categoryId: String): Flow<List<IRecipeEntity>>
}