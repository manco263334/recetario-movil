package com.dmm.recetario.data.repository

import com.dmm.recetario.core.utils.extension.isNotNull
import com.dmm.recetario.core.utils.handler.handleApiCall
import com.dmm.recetario.core.utils.helper.ResourceHelper
import com.dmm.recetario.core.utils.mapper.toDomain
import com.dmm.recetario.data.remote.retrofit.CategoryRemote
import com.dmm.recetario.domain.model.Category
import com.dmm.recetario.domain.repository.CategoryRepository

class CategoryRepositoryImpl (
    private val remote: CategoryRemote,
    private val resourceHelper: ResourceHelper
) : CategoryRepository {
    override suspend fun createCategory(data: Category): Category {
        val category = handleApiCall(resourceHelper) { remote.createCategory(data) }

        assert(category.isNotNull())

        return category!!.toDomain()
    }

    override suspend fun getAllCategories (
        page: Int,
        size: Int,
        withRecipes: Boolean?
    ): List<Category> {
        val categories = handleApiCall(resourceHelper) {
            remote.getAllCategories (
                page = page,
                size = size,
                withRecipes = withRecipes
            )
        }

        return categories?.content?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getCategory (
        id: String,
        withRecipes: Boolean?
    ): Category {
        val category = handleApiCall(resourceHelper) {
            remote.getCategory (
                id = id,
                withRecipes = withRecipes
            )
        }

        assert(category.isNotNull())

        return category!!.toDomain()
    }

    override suspend fun updateCategory (
        id: String,
        data: Category
    ): Category {
        val category = handleApiCall(resourceHelper) { remote.updateCategory(id, data) }

        assert(category.isNotNull())

        return category!!.toDomain()
    }

    override suspend fun deleteCategory(id: String) {
        handleApiCall(resourceHelper) { remote.deleteCategory(id) }
    }
}