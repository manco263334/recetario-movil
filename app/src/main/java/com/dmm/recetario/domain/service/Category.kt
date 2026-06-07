package com.dmm.recetario.domain.service

import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface CategoryService {
    suspend fun createCategory(data: Category): Category

    fun getAllCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Flow<List<Category>>

    fun getCategory (
        id: String,
        withRecipes: Boolean?
    ): Flow<Category?>

    suspend fun syncCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): Boolean

    suspend fun syncCategory(id: String, withRecipes: Boolean?): Boolean

    suspend fun updateCategory (
        id: String,
        data: Category
    ): Category

    suspend fun deleteCategory(id: String)

    fun getRecipes(categoryId: String): Flow<List<Recipe>>
}