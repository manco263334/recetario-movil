package com.dmm.recetario.domain.repository

import com.dmm.recetario.domain.model.Category

interface CategoryRepository {
    suspend fun createCategory(data: Category): Category

    suspend fun getAllCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): List<Category>

    suspend fun getCategory(id: String,withRecipes: Boolean?): Category

    suspend fun updateCategory(id: String,data: Category): Category

    suspend fun deleteCategory(id: String)
}