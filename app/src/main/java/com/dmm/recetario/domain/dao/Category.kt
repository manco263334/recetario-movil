package com.dmm.recetario.domain.dao

import com.dmm.recetario.data.local.database.entity.CategoryEntity
import com.dmm.recetario.data.local.database.entity.RecipeCategoryCrossRef
import com.dmm.recetario.data.local.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface CategoryDao {
    fun getCategories(): Flow<List<CategoryEntity>>

    fun getCategory(id: String): Flow<CategoryEntity?>

    suspend fun saveCategories(categories: List<CategoryEntity>)

    suspend fun saveCategory(category: CategoryEntity)

    suspend fun insertReferences(refs: List<RecipeCategoryCrossRef>)

    suspend fun insertReference(ref: RecipeCategoryCrossRef)

    suspend fun clear()

    suspend fun deleteCategory(id: String)

    fun getRecipes(categoryId: String): Flow<List<RecipeEntity>>
}